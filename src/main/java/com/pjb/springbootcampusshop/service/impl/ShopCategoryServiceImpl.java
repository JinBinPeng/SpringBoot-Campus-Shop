package com.pjb.springbootcampusshop.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjb.springbootcampusshop.cache.JedisUtil;
import com.pjb.springbootcampusshop.dao.ShopCategoryDao;
import com.pjb.springbootcampusshop.dto.ShopCategoryExecution;
import com.pjb.springbootcampusshop.entity.ShopCategory;
import com.pjb.springbootcampusshop.enums.ShopCategoryStateEnum;
import com.pjb.springbootcampusshop.service.ShopCategoryService;
import com.pjb.springbootcampusshop.util.FileUtil;
import com.pjb.springbootcampusshop.util.ImageUtil;

@Service
@Slf4j
public class ShopCategoryServiceImpl implements ShopCategoryService {
	private final JedisUtil.Strings jedisStrings;
	private final JedisUtil.Keys jedisKeys;
	private final ShopCategoryDao shopCategoryDao;
	private static final String SCLISTKEY = "shopcategorylist";

	@Autowired
	public ShopCategoryServiceImpl(JedisUtil.Strings jedisStrings, JedisUtil.Keys jedisKeys, ShopCategoryDao shopCategoryDao) {
		this.jedisStrings = jedisStrings;
		this.jedisKeys = jedisKeys;
		this.shopCategoryDao = shopCategoryDao;
	}

	@Override
	public List<ShopCategory> getFirstLevelShopCategoryList() throws IOException {
		String key = SCLISTKEY;
		List<ShopCategory> shopCategoryList;
		ObjectMapper mapper = new ObjectMapper();
		if (jedisKeys.exists(key)) {
			ShopCategory shopCategoryCondition = new ShopCategory();
			// 当shopCategoryId不为空的时候，查询的条件会变为 where parent_id is null
			shopCategoryCondition.setShopCategoryId(-1L);
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
			String jsonString = mapper.writeValueAsString(shopCategoryList);
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			shopCategoryList = mapper.readValue(jsonString, javaType);
		}
		return shopCategoryList;
	}

	@Override
	public List<ShopCategory> getShopCategoryList(Long parentId) throws IOException {
		String key = SCLISTKEY + "_" + parentId;
		List<ShopCategory> shopCategoryList;
		ObjectMapper mapper = new ObjectMapper();
		if (!jedisKeys.exists(key)) {
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setParentId(parentId);
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategory);
			String jsonString = mapper.writeValueAsString(shopCategoryList);
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			shopCategoryList = mapper.readValue(jsonString, javaType);
		}
		return shopCategoryList;
	}

	@Override
	public List<ShopCategory> getAllSecondLevelShopCategory() throws IOException {
		String key = SCLISTKEY + "ALLSECOND";
		List<ShopCategory> shopCategoryList;
		ObjectMapper mapper = new ObjectMapper();
		if (jedisKeys.exists(key)) {
			ShopCategory shopCategory = new ShopCategory();
			// 当shopCategoryDesc不为空的时候，查询的条件会变为 where parent_id is not null
			shopCategory.setShopCategoryDesc("ALLSECOND");
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategory);
			String jsonString = mapper.writeValueAsString(shopCategoryList);
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			shopCategoryList = mapper.readValue(jsonString, javaType);
		}
		return shopCategoryList;
	}

	@Override
	public ShopCategory getShopCategoryById(Long shopCategoryId) {
		List<ShopCategory> shopCategoryList = new ArrayList<>();
		try {
			shopCategoryList = getFirstLevelShopCategoryList();
			shopCategoryList.addAll(getAllSecondLevelShopCategory());
		} catch (IOException e) {
			log.warn(e.toString());
		}
		for (ShopCategory shopCategory : shopCategoryList) {
			if (shopCategoryId.equals(shopCategory.getShopCategoryId())) {
				return shopCategory;
			}
		}
		ShopCategory shopCategory = shopCategoryDao.queryShopCategoryById(shopCategoryId);
		if (shopCategory != null) {
			return shopCategory;
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public ShopCategoryExecution addShopCategory(ShopCategory shopCategory, CommonsMultipartFile thumbnail) {
		if (shopCategory != null) { shopCategory.setCreateTime(new Date());
			shopCategory.setLastEditTime(new Date());
			if (thumbnail != null) {
				addThumbnail(shopCategory, thumbnail);
			}
			try {
				int effectedNum = shopCategoryDao.insertShopCategory(shopCategory);
				return getShopCategoryExecution(shopCategory, effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("添加店铺类别信息失败:" + e.toString());
			}
		} else {
			return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
		}
	}

	private ShopCategoryExecution getShopCategoryExecution(ShopCategory shopCategory, int effectedNum) {
		if (effectedNum > 0) {
			String prefix = SCLISTKEY;
			Set<String> keySet = jedisKeys.keys(prefix + "*");
			for (String key : keySet) {
				jedisKeys.del(key);
			}
			return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
		} else {
			return new ShopCategoryExecution(
					ShopCategoryStateEnum.INNER_ERROR);
		}
	}

	@Override
	@Transactional
	public ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, CommonsMultipartFile thumbnail, boolean thumbnailChange) {
		if (shopCategory.getShopCategoryId() != null && shopCategory.getShopCategoryId() > 0) {
			shopCategory.setLastEditTime(new Date());
			if (thumbnail != null && thumbnailChange) {
				ShopCategory tempShopCategory = shopCategoryDao.queryShopCategoryById(shopCategory.getShopCategoryId());
				if (tempShopCategory.getShopCategoryImg() != null) {
					FileUtil.deleteFile(tempShopCategory.getShopCategoryImg());
				}
				addThumbnail(shopCategory, thumbnail);
			}
			try {
				int effectedNum = shopCategoryDao.updateShopCategory(shopCategory);
				return getShopCategoryExecution(shopCategory, effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("更新店铺类别信息失败:" + e.toString());
			}
		} else {
			return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	public ShopCategoryExecution removeShopCategory(long shopCategoryId) {
		if (shopCategoryId > 0) {
			try {
				ShopCategory tempShopCategory = shopCategoryDao.queryShopCategoryById(shopCategoryId);
				if (tempShopCategory.getShopCategoryImg() != null) {
					FileUtil.deleteFile(tempShopCategory.getShopCategoryImg());
				}
				int effectedNum = shopCategoryDao.deleteShopCategory(shopCategoryId);
				return getShopCategoryExecution(effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("删除店铺类别信息失败:" + e.toString());
			}
		} else {
			return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
		}
	}

	private ShopCategoryExecution getShopCategoryExecution(int effectedNum) {
		if (effectedNum > 0) {
			String prefix = SCLISTKEY;
			Set<String> keySet = jedisKeys.keys(prefix + "*");
			for (String key : keySet) {
				jedisKeys.del(key);
			}
			return new ShopCategoryExecution(
					ShopCategoryStateEnum.SUCCESS);
		} else {
			return new ShopCategoryExecution(
					ShopCategoryStateEnum.INNER_ERROR);
		}
	}

	@Override
	@Transactional
	public ShopCategoryExecution removeShopCategoryList(List<Long> shopCategoryIdList) {
		if (shopCategoryIdList != null && !shopCategoryIdList.isEmpty()) {
			try {
				List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategoryByIds(shopCategoryIdList);
				for (ShopCategory shopCategory : shopCategoryList) {
					if (shopCategory.getShopCategoryImg() != null) {
						FileUtil.deleteFile(shopCategory.getShopCategoryImg());
					}
				}
				int effectedNum = shopCategoryDao.batchDeleteShopCategory(shopCategoryIdList);
				return getShopCategoryExecution(effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("删除店铺类别信息失败:" + e.toString());
			}
		} else {
			return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
		}
	}

	private void addThumbnail(ShopCategory shopCategory, CommonsMultipartFile thumbnail) {
		String dest = FileUtil.getShopCategoryImagePath();
		String thumbnailAddr = ImageUtil.generateNormalImg(thumbnail, dest);
		shopCategory.setShopCategoryImg(thumbnailAddr);
	}
}
