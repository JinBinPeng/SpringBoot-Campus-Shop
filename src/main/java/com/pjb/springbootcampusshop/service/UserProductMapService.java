package com.pjb.springbootcampusshop.service;

import com.pjb.springbootcampusshop.dto.UserProductMapExecution;
import com.pjb.springbootcampusshop.entity.UserProductMap;

public interface UserProductMapService {

	UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex, Integer pageSize);

	UserProductMapExecution addUserProductMap(UserProductMap userProductMap);
}
