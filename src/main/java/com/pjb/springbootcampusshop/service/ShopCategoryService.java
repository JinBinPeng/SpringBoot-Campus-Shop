package com.pjb.springbootcampusshop.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pjb.springbootcampusshop.dto.ShopCategoryExecution;
import com.pjb.springbootcampusshop.entity.ShopCategory;

public interface ShopCategoryService {
	/**
	 * 查询指定某个店铺下的所有商品类别信息
	 */
	List<ShopCategory> getFirstLevelShopCategoryList() throws IOException;

	List<ShopCategory> getShopCategoryList(Long parentId) throws IOException;

	List<ShopCategory> getAllSecondLevelShopCategory() throws IOException;

	ShopCategory getShopCategoryById(Long shopCategoryId);

	ShopCategoryExecution addShopCategory(ShopCategory shopCategory, CommonsMultipartFile thumbnail);

	ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, CommonsMultipartFile thumbnail, boolean thumbnailChange);

	ShopCategoryExecution removeShopCategory(long shopCategoryId);

	ShopCategoryExecution removeShopCategoryList(List<Long> shopCategoryIdList);

}
