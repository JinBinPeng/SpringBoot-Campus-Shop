package com.pjb.springbootcampusshop.web.superadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjb.springbootcampusshop.dto.ShopExecution;
import com.pjb.springbootcampusshop.entity.ConstantForSuperAdmin;
import com.pjb.springbootcampusshop.entity.Shop;
import com.pjb.springbootcampusshop.entity.ShopCategory;
import com.pjb.springbootcampusshop.enums.ShopStateEnum;
import com.pjb.springbootcampusshop.service.ShopCategoryService;
import com.pjb.springbootcampusshop.service.ShopService;
import com.pjb.springbootcampusshop.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/superadmin")
public class ShopController {
	private final ShopService shopService;
	private final ShopCategoryService shopCategoryService;

	@Autowired
	public ShopController(ShopService shopService, ShopCategoryService shopCategoryService) {
		this.shopService = shopService;
		this.shopCategoryService = shopCategoryService;
	}

	@PostMapping("/listshops")
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		ShopExecution shops;
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		if (pageIndex > 0 && pageSize > 0) {
			Shop shopCondition = new Shop();
			int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
			if (enableStatus >= 0) {
				shopCondition.setEnableStatus(enableStatus);
			}
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			if (shopCategoryId > 0) {
				ShopCategory sc = new ShopCategory();
				sc.setShopCategoryId(shopCategoryId);
				shopCondition.setShopCategory(sc);
			}
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			if (shopName != null) {
				try {
					shopCondition.setShopName(URLDecoder.decode(shopName, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				}
			}
			try {
				shops = shopService.getShopList(shopCondition, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (shops.getShopList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, shops.getShopList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, shops.getCount());
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<Shop>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}

	@PostMapping("/searchshopbyid")
	private Map<String, Object> searchShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		Shop shop;
		int pageIndex = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_SIZE);
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (pageIndex > 0 && pageSize > 0 && shopId > 0) {
			try {
				shop = shopService.getByShopId(shopId);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (shop != null) {
				List<Shop> shopList = new ArrayList<>();
				shopList.add(shop);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, shopList);
				modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE,
						new ArrayList<Shop>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}

	@PostMapping("/modifyshop")
	private Map<String, Object> modifyShop(String shopStr) {
		Map<String, Object> modelMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		Shop shop;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (shop != null && shop.getShopId() != null) {
			try {
				shop.setShopName((shop.getShopName() == null) ? null
						: (URLDecoder.decode(shop.getShopName(), "UTF-8")));
				shop.setShopDesc((shop.getShopDesc() == null) ? null
						: (URLDecoder.decode(shop.getShopDesc(), "UTF-8")));
				shop.setShopAddr((shop.getShopAddr() == null) ? null
						: (URLDecoder.decode(shop.getShopAddr(), "UTF-8")));
				shop.setAdvice((shop.getAdvice() == null) ? null : (URLDecoder
						.decode(shop.getAdvice(), "UTF-8")));
				if (shop.getShopCategoryId() != null) {
					ShopCategory sc = shopCategoryService
							.getShopCategoryById(shop.getShopCategoryId());
					if (sc != null) {
						ShopCategory parentCategory = new ShopCategory();
						parentCategory.setShopCategoryId(sc.getParentId());
						shop.setParentCategory(parentCategory);
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", "输入非法的子类别");
					}
				}
				ShopExecution ae = shopService.modifyShop(shop, null);
				if (ae.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
		}
		return modelMap;
	}

}
