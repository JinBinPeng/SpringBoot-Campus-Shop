package com.pjb.springbootcampusshop.web.frontend;

import com.pjb.springbootcampusshop.dto.UserShopMapExecution;
import com.pjb.springbootcampusshop.entity.UserShopMap;
import com.pjb.springbootcampusshop.service.UserShopMapService;
import com.pjb.springbootcampusshop.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class MyShopPointController {
	private final UserShopMapService userShopMapService;

	@Autowired
	public MyShopPointController(UserShopMapService userShopMapService) {
		this.userShopMapService = userShopMapService;
	}

	@GetMapping("/listusershopmapsbycustomer")
	private Map<String, Object> listUserShopMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		Long userId = 1L;
		if (pageIndex > -1 && pageSize > -1) {
			UserShopMap userShopMapCondition = new UserShopMap();
			userShopMapCondition.setUserId(userId);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			if (shopId > -1) {
				userShopMapCondition.setShopId(shopId);
			}
			UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
			modelMap.put("userShopMapList", ue.getUserShopMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
}
