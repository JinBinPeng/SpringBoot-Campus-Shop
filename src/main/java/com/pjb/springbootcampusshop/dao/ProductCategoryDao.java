package com.pjb.springbootcampusshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pjb.springbootcampusshop.entity.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public interface ProductCategoryDao {
	/**
	 * 通过employee id 查询店铺
	 */
	List<ProductCategory> queryByShopId(long shopId);

	/**
	 * 新增商品类别
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);

	/**
	 * 删除商品类别（初版，即只支持删除尚且没有发布商品的商品类别）
	 */
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);
}
