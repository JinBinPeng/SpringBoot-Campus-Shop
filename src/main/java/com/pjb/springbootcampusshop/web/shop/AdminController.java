package com.pjb.springbootcampusshop.web.shop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pjb.springbootcampusshop.entity.Shop;
import com.pjb.springbootcampusshop.util.HttpServletRequestUtil;

@Controller
@RequestMapping("shop")
public class AdminController {
	@RequestMapping(value = "/test")
	public Map<String, Object> productcategory(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		String kaptchaExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		System.out.println(kaptchaExpected);
		modelMap.put("verifyCode", kaptchaExpected);
		return modelMap;
	}

	@RequestMapping(value = "/ownerlogin")
	public String ownerLogin() {
		return "shop/ownerlogin";
	}

	@GetMapping("/register")
	private String register() {
		return "shop/register";
	}

	@GetMapping("/changepsw")
	private String changePsw() {
		return "shop/changepsw";
	}

	@GetMapping("/ownerbind")
	private String ownerBind() {
		return "shop/ownerbind";
	}

	@GetMapping("/shoplist")
	private String myList() {
		return "shop/shoplist";
	}

	@GetMapping("/shopmanage")
	private String shopManage(HttpServletRequest request) {
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId <= 0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null) {
				return "shop/shoplist";
			} else {
				return "shop/shopmanage";
			}
		} else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			return "shop/shopmanage";
		}
	}

	@GetMapping("/shopedit")
	private String shopEdit() {
		return "shop/shopedit";
	}

	@GetMapping("/productmanage")
	private String productManage() {
		return "shop/productmanage";
	}

	@GetMapping("/productedit")
	private String productEdit() {
		return "shop/productedit";
	}

	@GetMapping("/productcategorymanage")
	private String productCategoryManage() {
		return "shop/productcategorymanage";
	}

	@GetMapping("/shopauthmanage")
	private String shopAuthManage() {
		return "shop/shopauthmanage";
	}

	@GetMapping("/shopauthedit")
	private String shopAuthEdit() {
		return "shop/shopauthedit";
	}

	@GetMapping("/productbuycheck")
	private String productBuyCheck() {
		return "shop/productbuycheck";
	}

	@GetMapping("/awarddelivercheck")
	private String awardDeliverCheck() {
		return "shop/awarddelivercheck";
	}

	@GetMapping("/usershopcheck")
	private String userShopCheck() {
		return "shop/usershopcheck";
	}

	@GetMapping("/awardmanage")
	private String awardManage() {
		return "shop/awardmanage";
	}

	@GetMapping("/awardedit")
	private String awardEdit() {
		return "shop/awardedit";
	}

	@GetMapping("/customermanage")
	private String customerManage() {
		return "shop/customermanage";
	}
}
