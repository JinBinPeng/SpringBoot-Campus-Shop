package com.pjb.springbootcampusshop.web.superadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminController {

	@GetMapping("/areamanage")
	private String areaManagement() {
		return "superadmin/areamanage";
	}

	@GetMapping("/headlinemanage")
	private String headLineManagement() {
		return "superadmin/headlinemanage";
	}

	@GetMapping("/shopcategorymanage")
	private String shopCategoryManage() {
		return "superadmin/shopcategorymanage";
	}

	@GetMapping("/shopmanage")
	private String shopManage() {
		return "superadmin/shopmanage";
	}

	@GetMapping("/personinfomanage")
	private String personInfoManage() {
		return "superadmin/personinfomanage";
	}

	@GetMapping("/main")
	private String main() {
		return "superadmin/main";
	}

	@GetMapping("/top")
	private String top() {
		return "superadmin/top";
	}

	@GetMapping("/login")
	private String login() {
		return "superadmin/login";
	}

}
