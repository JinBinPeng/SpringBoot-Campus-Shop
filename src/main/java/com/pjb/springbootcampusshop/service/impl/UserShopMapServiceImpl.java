package com.pjb.springbootcampusshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pjb.springbootcampusshop.dao.UserShopMapDao;
import com.pjb.springbootcampusshop.dto.UserShopMapExecution;
import com.pjb.springbootcampusshop.entity.UserShopMap;
import com.pjb.springbootcampusshop.service.UserShopMapService;
import com.pjb.springbootcampusshop.util.PageCalculator;

@Service
public class UserShopMapServiceImpl implements UserShopMapService {
	private final UserShopMapDao userShopMapDao;

	@Autowired
	public UserShopMapServiceImpl(UserShopMapDao userShopMapDao) {
		this.userShopMapDao = userShopMapDao;
	}

	@Override
	public UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize) {
		if (userShopMapCondition != null && pageIndex != -1 && pageSize != -1) {
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMapCondition, beginIndex, pageSize);
			int count = userShopMapDao.queryUserShopMapCount(userShopMapCondition);
			UserShopMapExecution userShopMapExecution = new UserShopMapExecution();
			userShopMapExecution.setUserShopMapList(userShopMapList);
			userShopMapExecution.setCount(count);
			return userShopMapExecution;
		} else {
			return null;
		}

	}
}
