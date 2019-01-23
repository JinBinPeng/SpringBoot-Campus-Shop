package com.pjb.springbootcampusshop.interceptor.shop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.pjb.springbootcampusshop.entity.Shop;

public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
		if (currentShop != null && shopList != null) {
			for (Shop shop : shopList) {
				if (shop.getShopId().equals(currentShop.getShopId())) {
					return true;
				}
			}
		}
		return false;
	}
}
