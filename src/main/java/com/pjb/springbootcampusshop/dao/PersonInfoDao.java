package com.pjb.springbootcampusshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pjb.springbootcampusshop.entity.PersonInfo;
import org.springframework.stereotype.Component;

@Component
public interface PersonInfoDao {

	List<PersonInfo> queryPersonInfoList(@Param("personInfoCondition") PersonInfo personInfoCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);


	int queryPersonInfoCount(@Param("personInfoCondition") PersonInfo personInfoCondition);

	PersonInfo queryPersonInfoById(long userId);

	int insertPersonInfo(PersonInfo personInfo);

	int updatePersonInfo(PersonInfo personInfo);

	int deletePersonInfo(long userId);
}
