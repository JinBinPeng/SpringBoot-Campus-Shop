package com.pjb.springbootcampusshop.service.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pjb.springbootcampusshop.dao.ProductCategoryDao;
import com.pjb.springbootcampusshop.dao.ProductDao;
import com.pjb.springbootcampusshop.dto.ProductCategoryExecution;
import com.pjb.springbootcampusshop.entity.ProductCategory;
import com.pjb.springbootcampusshop.enums.ProductCategoryStateEnum;
import com.pjb.springbootcampusshop.service.ProductCategoryService;

@Slf4j
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	private final ProductCategoryDao productCategoryDao;
	private final ProductDao productDao;

	@Autowired
	public ProductCategoryServiceImpl(ProductCategoryDao productCategoryDao, ProductDao productDao) {
		this.productCategoryDao = productCategoryDao;
		this.productDao = productDao;
	}

	@Override
	public List<ProductCategory> getByShopId(long shopId) {
		return productCategoryDao.queryByShopId(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)  {
		if (!productCategoryList.isEmpty()) {
			try {
				int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if (effectedNum <= 0) {
					throw new RuntimeException("店铺类别失败");
				} else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new RuntimeException("batchAddProductCategory error: " + e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
		}

	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)  {
		try {
			int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
			if (effectedNum < 0) {
				throw new RuntimeException("商品类别更新失败");
			}
		} catch (Exception e) {
			throw new RuntimeException("deleteProductCategory error: " + e.getMessage());
		}
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectedNum <= 0) {
				throw new RuntimeException("店铺类别删除失败");
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}

		} catch (Exception e) {
			throw new RuntimeException("deleteProductCategory error: " + e.getMessage());
		}
	}

}
