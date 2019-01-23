package com.pjb.springbootcampusshop.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
	@GetMapping("/mainpage")
	private String showMainPage() {
		return "frontend/mainpage";
	}

	@GetMapping("/productdetail")
	private String showProductDetail() {
		return "frontend/productdetail";
	}

	@GetMapping("/shopdetail")
	private String showShopDetail() {
		return "frontend/shopdetail";
	}

	@GetMapping("/shoplist")
	private String showShopList() {
		return "frontend/shoplist";
	}

	@GetMapping("/index")
	private String index() {
		return "frontend/index";
	}

	@GetMapping("/mypoint")
	private String myPoint() {
		return "frontend/mypoint";
	}

	@GetMapping("/myrecord")
	private String myRecord() {
		return "frontend/myrecord";
	}

	@GetMapping("/pointrecord")
	private String pointRecord() {
		return "frontend/pointrecord";
	}

	@GetMapping("/awarddetail")
	private String awardDetail() {
		return "frontend/awarddetail";
	}

	@GetMapping("/customerbind")
	private String customerBind() {
		return "frontend/customerbind";
	}
}
