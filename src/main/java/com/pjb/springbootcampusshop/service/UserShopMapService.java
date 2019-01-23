package com.pjb.springbootcampusshop.service;

import com.pjb.springbootcampusshop.dto.UserShopMapExecution;
import com.pjb.springbootcampusshop.entity.UserShopMap;

public interface UserShopMapService {

	UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize);

}
