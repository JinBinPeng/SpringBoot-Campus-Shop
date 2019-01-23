package com.pjb.springbootcampusshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pjb.springbootcampusshop.entity.ShopAuthMap;
import org.springframework.stereotype.Component;

@Component
public interface ShopAuthMapDao {
	/**
	 * 分页列出店铺下面的授权信息
	 */
	List<ShopAuthMap> queryShopAuthMapListByShopId(@Param("shopId") long shopId, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * 获取授权总数
	 */
	int queryShopAuthCountByShopId(@Param("shopId") long shopId);

	/**
	 * 新增一条店铺与店员的授权关系
	 */
	int insertShopAuthMap(ShopAuthMap shopAuthMap);

	/**
	 * 更新授权信息
	 */
	int updateShopAuthMap(ShopAuthMap shopAuthMap);

	/**
	 * 对某员工除权
	 */
	int deleteShopAuthMap(@Param("employeeId") long employeeId, @Param("shopId") long shopId);

	ShopAuthMap queryShopAuthMapById(Long shopAuthId);
}
