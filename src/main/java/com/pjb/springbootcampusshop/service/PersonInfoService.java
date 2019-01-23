package com.pjb.springbootcampusshop.service;

import com.pjb.springbootcampusshop.dto.PersonInfoExecution;
import com.pjb.springbootcampusshop.entity.PersonInfo;

public interface PersonInfoService {

	PersonInfo getPersonInfoById(Long userId);

	PersonInfoExecution getPersonInfoList(PersonInfo personInfoCondition, int pageIndex, int pageSize);

	PersonInfoExecution addPersonInfo(PersonInfo personInfo);

	PersonInfoExecution modifyPersonInfo(PersonInfo personInfo);

}
