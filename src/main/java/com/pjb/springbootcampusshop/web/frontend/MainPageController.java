package com.pjb.springbootcampusshop.web.frontend;

import com.pjb.springbootcampusshop.entity.HeadLine;
import com.pjb.springbootcampusshop.entity.ShopCategory;
import com.pjb.springbootcampusshop.enums.HeadLineStateEnum;
import com.pjb.springbootcampusshop.enums.ShopCategoryStateEnum;
import com.pjb.springbootcampusshop.service.HeadLineService;
import com.pjb.springbootcampusshop.service.ShopCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
@Slf4j
public class MainPageController {
	private final ShopCategoryService shopCategoryService;
	private final HeadLineService headLineService;

	@Autowired
	public MainPageController(ShopCategoryService shopCategoryService, HeadLineService headLineService) {
		this.shopCategoryService = shopCategoryService;
		this.headLineService = headLineService;
	}

	@GetMapping("/listmainpageinfo")
	private Map<String, Object> list1stShopCategory() {
		Map<String, Object> modelMap = new HashMap<>();
		try {
			List<ShopCategory> shopCategoryList = shopCategoryService.getFirstLevelShopCategoryList();
			modelMap.put("shopCategoryList", shopCategoryList);
		} catch (Exception e) {
			log.warn(e.toString());
			ShopCategoryStateEnum s = ShopCategoryStateEnum.INNER_ERROR;
			modelMap.put("success", false);
			modelMap.put("errMsg", s.getStateInfo());
			return modelMap;
		}
		try {
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			List<HeadLine> headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		} catch (Exception e) {
			log.warn(e.toString());
			HeadLineStateEnum s = HeadLineStateEnum.INNER_ERROR;
			modelMap.put("success", false);
			modelMap.put("errMsg", s.getStateInfo());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}

}
