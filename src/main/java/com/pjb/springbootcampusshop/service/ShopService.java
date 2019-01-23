package com.pjb.springbootcampusshop.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.pjb.springbootcampusshop.dto.ShopExecution;
import com.pjb.springbootcampusshop.entity.Shop;

public interface ShopService {

	ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

	/**
	 * 查询该用户下面的店铺信息
	 */
	ShopExecution getByEmployeeId(long employeeId) ;

	/**
	 * 查询指定店铺信息
	 */
	Shop getByShopId(long shopId);

	/**
	 * 创建商铺
	 */
	ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg) ;

	/**
	 * 更新店铺信息（从店家角度）
	 */
	ShopExecution modifyShop(Shop shop, CommonsMultipartFile shopImg) ;

}
