package com.pjb.springbootcampusshop.service;

import java.util.List;

import com.pjb.springbootcampusshop.dto.ProductCategoryExecution;
import com.pjb.springbootcampusshop.entity.ProductCategory;

public interface ProductCategoryService {
	/**
	 * 查询指定某个店铺下的所有商品类别信息
	 */
	List<ProductCategory> getByShopId(long shopId);

	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) ;

	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) ;
}
