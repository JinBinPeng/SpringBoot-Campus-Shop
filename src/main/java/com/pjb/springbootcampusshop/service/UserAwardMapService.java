package com.pjb.springbootcampusshop.service;

import com.pjb.springbootcampusshop.dto.UserAwardMapExecution;
import com.pjb.springbootcampusshop.entity.UserAwardMap;

public interface UserAwardMapService {

	UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize);

	UserAwardMap getUserAwardMapById(long userAwardMapId);

	UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap);

	UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap);

}
