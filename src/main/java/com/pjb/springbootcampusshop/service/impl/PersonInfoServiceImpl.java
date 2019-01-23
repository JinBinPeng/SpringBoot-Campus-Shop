package com.pjb.springbootcampusshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pjb.springbootcampusshop.dao.PersonInfoDao;
import com.pjb.springbootcampusshop.dto.PersonInfoExecution;
import com.pjb.springbootcampusshop.entity.PersonInfo;
import com.pjb.springbootcampusshop.enums.PersonInfoStateEnum;
import com.pjb.springbootcampusshop.service.PersonInfoService;
import com.pjb.springbootcampusshop.util.PageCalculator;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
	private final PersonInfoDao personInfoDao;

	@Autowired
	public PersonInfoServiceImpl(PersonInfoDao personInfoDao) {
		this.personInfoDao = personInfoDao;
	}

	@Override
	public PersonInfo getPersonInfoById(Long userId) {
		return personInfoDao.queryPersonInfoById(userId);
	}

	@Override
	public PersonInfoExecution getPersonInfoList(PersonInfo personInfoCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<PersonInfo> personInfoList = personInfoDao.queryPersonInfoList(personInfoCondition, rowIndex, pageSize);
		int count = personInfoDao.queryPersonInfoCount(personInfoCondition);
		PersonInfoExecution personInfoExecution = new PersonInfoExecution();
		if (personInfoList != null) {
			personInfoExecution.setPersonInfoList(personInfoList);
			personInfoExecution.setCount(count);
		} else {
			personInfoExecution.setState(PersonInfoStateEnum.INNER_ERROR.getState());
		}
		return personInfoExecution;
	}

	@Override
	@Transactional
	public PersonInfoExecution addPersonInfo(PersonInfo personInfo) {
		if (personInfo == null) {
			return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
		} else {
			try {
				int effectedNum = personInfoDao.insertPersonInfo(personInfo);
				return getPersonInfoExecution(personInfo, effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("addPersonInfo error: " + e.getMessage());
			}
		}
	}

	private PersonInfoExecution getPersonInfoExecution(PersonInfo personInfo, int effectedNum) {
		if (effectedNum <= 0) {
			return new PersonInfoExecution(PersonInfoStateEnum.INNER_ERROR);
		} else {// 创建成功
			personInfo = personInfoDao.queryPersonInfoById(personInfo.getUserId());
			return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, personInfo);
		}
	}

	@Override
	@Transactional
	public PersonInfoExecution modifyPersonInfo(PersonInfo personInfo) {
		if (personInfo == null || personInfo.getUserId() == null) {
			return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
		} else {
			try {
				int effectedNum = personInfoDao.updatePersonInfo(personInfo);
				return getPersonInfoExecution(personInfo, effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("updatePersonInfo error: " + e.getMessage());
			}
		}
	}

}
