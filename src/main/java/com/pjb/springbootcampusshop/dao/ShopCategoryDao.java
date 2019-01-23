package com.pjb.springbootcampusshop.dao;

import com.pjb.springbootcampusshop.entity.ShopCategory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ShopCategoryDao {

	List<ShopCategory> queryShopCategory(ShopCategory shopCategory);

	ShopCategory queryShopCategoryById(long shopCategoryId);

	List<ShopCategory> queryShopCategoryByIds(List<Long> shopCategoryIdList);

	int insertShopCategory(ShopCategory shopCategory);

	int updateShopCategory(ShopCategory shopCategory);

	int deleteShopCategory(long shopCategoryId);

	int batchDeleteShopCategory(List<Long> shopCategoryIdList);

}
