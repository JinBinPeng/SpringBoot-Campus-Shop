package com.pjb.springbootcampusshop.service;

import com.pjb.springbootcampusshop.dto.ShopAuthMapExecution;
import com.pjb.springbootcampusshop.entity.ShopAuthMap;

public interface ShopAuthMapService {

	ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize);

	ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap);

	/**
	 * 更新授权信息，包括职位等
	 */
	ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) ;

	ShopAuthMapExecution removeShopAuthMap(Long shopAuthMapId);

	ShopAuthMap getShopAuthMapById(Long shopAuthId);

}
