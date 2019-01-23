package com.pjb.springbootcampusshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pjb.springbootcampusshop.entity.UserProductMap;
import org.springframework.stereotype.Component;

@Component
public interface UserProductMapDao {

	List<UserProductMap> queryUserProductMapList(@Param("userProductCondition") UserProductMap userProductCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	int queryUserProductMapCount(@Param("userProductCondition") UserProductMap userProductCondition);

	int insertUserProductMap(UserProductMap userProductMap);
}
