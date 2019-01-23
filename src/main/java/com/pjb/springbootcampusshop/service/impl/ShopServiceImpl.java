package com.pjb.springbootcampusshop.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.pjb.springbootcampusshop.dao.ShopAuthMapDao;
import com.pjb.springbootcampusshop.dao.ShopCategoryDao;
import com.pjb.springbootcampusshop.dao.ShopDao;
import com.pjb.springbootcampusshop.dto.ShopExecution;
import com.pjb.springbootcampusshop.entity.Shop;
import com.pjb.springbootcampusshop.entity.ShopAuthMap;
import com.pjb.springbootcampusshop.entity.ShopCategory;
import com.pjb.springbootcampusshop.enums.ShopStateEnum;
import com.pjb.springbootcampusshop.service.ShopService;
import com.pjb.springbootcampusshop.util.FileUtil;
import com.pjb.springbootcampusshop.util.ImageUtil;
import com.pjb.springbootcampusshop.util.PageCalculator;

@Service
public class ShopServiceImpl implements ShopService {
	private final ShopDao shopDao;
	private final ShopAuthMapDao shopAuthMapDao;
	private final ShopCategoryDao shopCategoryDao;

	@Autowired
	public ShopServiceImpl(ShopDao shopDao, ShopAuthMapDao shopAuthMapDao, ShopCategoryDao shopCategoryDao) {
		this.shopDao = shopDao;
		this.shopAuthMapDao = shopAuthMapDao;
		this.shopCategoryDao = shopCategoryDao;
	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution shopExecution = new ShopExecution();
		if (shopList != null) {
			shopExecution.setShopList(shopList);
			shopExecution.setCount(count);
		} else {
			shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return shopExecution;
	}

	@Override
	public ShopExecution getByEmployeeId(long employeeId) {
		List<Shop> shopList = shopDao.queryByEmployeeId(employeeId);
		ShopExecution shopExecution = new ShopExecution();
		shopExecution.setShopList(shopList);
		return shopExecution;
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	@Transactional
	/**
	 * 使用注解控制事务方法的优点： 1.开发团队达成一致约定，明确标注事务方法的编程风格
	 * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
	 * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
	 */
	public ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg) {
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		}
		try {
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			if (shop.getShopCategory() != null) {
				Long shopCategoryId = shop.getShopCategory().getShopCategoryId();
				ShopCategory shopCategory= shopCategoryDao.queryShopCategoryById(shopCategoryId);
				ShopCategory parentCategory = new ShopCategory();
				parentCategory.setShopCategoryId(shopCategory.getParentId());
				shop.setParentCategory(parentCategory);
			}
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				throw new RuntimeException("店铺创建失败");
			} else {
				try {
					if (shopImg != null) {
						addShopImg(shop, shopImg);
						effectedNum = shopDao.updateShop(shop);
						if (effectedNum <= 0) {
							throw new RuntimeException("创建图片地址失败");
						}
					}
				} catch (Exception e) {
					throw new RuntimeException("addShopImg error: " + e.getMessage());
				}
				// 执行增加shopAuthMap操作
				ShopAuthMap shopAuthMap = new ShopAuthMap();
				shopAuthMap.setEmployeeId(shop.getOwnerId());
				shopAuthMap.setShopId(shop.getShopId());
				shopAuthMap.setName("");
				shopAuthMap.setTitle("Owner");
				shopAuthMap.setTitleFlag(1);
				shopAuthMap.setCreateTime(new Date());
				shopAuthMap.setLastEditTime(new Date());
				shopAuthMap.setEnableStatus(1);
				try {
					effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
					if (effectedNum <= 0) {
						throw new RuntimeException("授权创建失败");
					} else {// 创建成功
						return new ShopExecution(ShopStateEnum.CHECK, shop);
					}
				} catch (Exception e) {
					throw new RuntimeException("insertShopAuthMap error: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			throw new RuntimeException("insertShop error: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public ShopExecution modifyShop(Shop shop, CommonsMultipartFile shopImg) {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOPID);
		} else {
			try {
				if (shopImg != null) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg() != null) {
						FileUtil.deleteFile(tempShop.getShopImg());
					}
					addShopImg(shop, shopImg);
				}
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {// 创建成功
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new RuntimeException("modifyShop error: " + e.getMessage());
			}
		}
	}

	private void addShopImg(Shop shop, CommonsMultipartFile shopImg) {
		String dest = FileUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);
		shop.setShopImg(shopImgAddr);
	}

}
