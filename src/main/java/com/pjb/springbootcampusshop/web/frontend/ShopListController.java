package com.pjb.springbootcampusshop.web.frontend;

import com.pjb.springbootcampusshop.dto.ShopExecution;
import com.pjb.springbootcampusshop.entity.Area;
import com.pjb.springbootcampusshop.entity.Shop;
import com.pjb.springbootcampusshop.entity.ShopCategory;
import com.pjb.springbootcampusshop.service.AreaService;
import com.pjb.springbootcampusshop.service.ShopCategoryService;
import com.pjb.springbootcampusshop.service.ShopService;
import com.pjb.springbootcampusshop.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class ShopListController {
	private final AreaService areaService;
	private final ShopCategoryService shopCategoryService;
	private final ShopService shopService;
	private static final String SUCCESS="success";
	private static final String ERRMSG="errMsg";
	@Autowired
	public ShopListController(AreaService areaService, ShopCategoryService shopCategoryService, ShopService shopService) {
		this.areaService = areaService;
		this.shopCategoryService = shopCategoryService;
		this.shopService = shopService;
	}

	@GetMapping("/listshopspageinfo")
	private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		if (parentId != -1) {
			try {
				shopCategoryList = shopCategoryService.getShopCategoryList(parentId);
			} catch (IOException e) {
				modelMap.put(SUCCESS, false);
				modelMap.put(ERRMSG, e.toString());
			}
		} else {
			try {
				shopCategoryList = shopCategoryService.getFirstLevelShopCategoryList();
			} catch (IOException e) {
				modelMap.put(SUCCESS, false);
				modelMap.put(ERRMSG, e.toString());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		List<Area> areaList;
		try {
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put(SUCCESS, true);
			return modelMap;
		} catch (IOException e) {
			modelMap.put(SUCCESS, false);
			modelMap.put(ERRMSG, e.toString());
		}
		return modelMap;
	}

	@GetMapping("/listshops")
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		if ((pageIndex > -1) && (pageSize > -1)) {
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");			
			long shopCategoryId = HttpServletRequestUtil.getLong(request,
					"shopCategoryId");
			long areaId = HttpServletRequestUtil.getLong(request, "areaId");
			String shopName = HttpServletRequestUtil.getString(request,
					"shopName");
			Shop shopCondition = compactShopCondition4Search(parentId,
					shopCategoryId, areaId, shopName);
			ShopExecution se = shopService.getShopList(shopCondition,
					pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put(SUCCESS, true);
		} else {
			modelMap.put(SUCCESS, false);
			modelMap.put(ERRMSG, "empty pageSize or pageIndex");
		}

		return modelMap;
	}

	private Shop compactShopCondition4Search(long parentId,
			long shopCategoryId, long areaId, String shopName) {
		Shop shopCondition = new Shop();
		if (parentId != -1L) {
			ShopCategory parentCategory = new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			shopCondition.setParentCategory(parentCategory);
		}
		if (shopCategoryId != -1L) {
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		if (areaId != -1L) {
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}

		if (shopName != null) {
			shopCondition.setShopName(shopName);
		}
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}
}
