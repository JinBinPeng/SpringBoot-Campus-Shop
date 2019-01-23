package com.pjb.springbootcampusshop.web.frontend;

import com.pjb.springbootcampusshop.dto.UserProductMapExecution;
import com.pjb.springbootcampusshop.entity.PersonInfo;
import com.pjb.springbootcampusshop.entity.UserProductMap;
import com.pjb.springbootcampusshop.service.UserProductMapService;
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
public class MyProductController {
	private final UserProductMapService userProductMapService;

	@Autowired
	public MyProductController(UserProductMapService userProductMapService) {
		this.userProductMapService = userProductMapService;
	}

	@GetMapping("/listuserproductmapsbycustomer")
	private Map<String, Object> listUserProductMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != -1)) {
			UserProductMap userProductMapCondition = new UserProductMap();
			userProductMapCondition.setUserId(user.getUserId());
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			if (shopId > -1) {
				userProductMapCondition.setShopId(shopId);
			}
			String productName = HttpServletRequestUtil.getString(request, "productName");
			if (productName != null) {
				userProductMapCondition.setProductName(productName);
			}
			UserProductMapExecution userProductMapExecution = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex, pageSize);
			modelMap.put("userProductMapList", userProductMapExecution.getUserProductMapList());
			modelMap.put("count", userProductMapExecution.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
}
