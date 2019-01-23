package com.pjb.springbootcampusshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pjb.springbootcampusshop.entity.UserAwardMap;
import org.springframework.stereotype.Component;

@Component
public interface UserAwardMapDao {

	List<UserAwardMap> queryUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAwardCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	int queryUserAwardMapCount(@Param("userAwardCondition") UserAwardMap userAwardCondition);

	UserAwardMap queryUserAwardMapById(long userAwardId);

	int insertUserAwardMap(UserAwardMap userAwardMap);

	int updateUserAwardMap(UserAwardMap userAwardMap);
}
