package com.pjb.springbootcampusshop.web.wechat;

import com.pjb.springbootcampusshop.dto.ShopExecution;
import com.pjb.springbootcampusshop.dto.WechatAuthExecution;
import com.pjb.springbootcampusshop.entity.PersonInfo;
import com.pjb.springbootcampusshop.entity.WechatAuth;
import com.pjb.springbootcampusshop.enums.WechatAuthStateEnum;
import com.pjb.springbootcampusshop.service.PersonInfoService;
import com.pjb.springbootcampusshop.service.ShopAuthMapService;
import com.pjb.springbootcampusshop.service.ShopService;
import com.pjb.springbootcampusshop.service.WechatAuthService;
import com.pjb.springbootcampusshop.util.weixin.WeiXinUser;
import com.pjb.springbootcampusshop.util.weixin.WeiXinUserUtil;
import com.pjb.springbootcampusshop.util.weixin.message.pojo.UserAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("wechatlogin")
/**
 * 从微信菜单点击后调用的接口，可以在url里增加参数（role_type）来表明是从商家还是从玩家按钮进来的，依次区分登陆后跳转不同的页面
 * 玩家会跳转到index.html页面
 * 商家如果没有注册，会跳转到注册页面，否则跳转到任务管理页面
 * 如果是商家的授权用户登陆，会跳到授权店铺的任务管理页面
 * @author lixiang
 *
 */
public class WeiXinLoginController {

	private static Logger log = LoggerFactory
			.getLogger(WeiXinLoginController.class);

	@Resource
	private PersonInfoService personInfoService;
	@Resource
	private WechatAuthService WechatAuthService;

	@Resource
	private ShopService shopService;

	@Resource
	private ShopAuthMapService shopAuthMapService;

	private static final String FRONTEND = "1";
	private static final String SHOPEND = "2";

	@GetMapping("/logincheck")
	public String doGet(HttpServletRequest request) {
		log.debug("weixin login get...");
		String code = request.getParameter("code");
		String roleType = request.getParameter("state");
		log.debug("weixin login code:" + code);
		WechatAuth auth = null;
		WeiXinUser user = null;
		String openId = null;
		if (null != code) {
			UserAccessToken token;
			try {
				token = WeiXinUserUtil.getUserAccessToken(code);
				log.debug("weixin login token:" + token.toString());
				String accessToken = token.getAccessToken();
				openId = token.getOpenId();
				user = WeiXinUserUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				auth = WechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		log.debug("weixin login success.");
		log.debug("login role_type:" + roleType);
		if (FRONTEND.equals(roleType)) {
			PersonInfo personInfo = WeiXinUserUtil
					.getPersonInfoFromRequest(user);
			if (auth == null) {
				personInfo.setCustomerFlag(1);
				auth = new WechatAuth();
				auth.setOpenId(openId);
				auth.setPersonInfo(personInfo);
				WechatAuthExecution we = WechatAuthService.register(auth, null);
				if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
					return null;
				}
			}
			personInfo = personInfoService.getPersonInfoById(auth.getUserId());
			request.getSession().setAttribute("user", personInfo);
			return "frontend/index";
		}
		if (SHOPEND.equals(roleType)) {
			PersonInfo personInfo;
			WechatAuthExecution we;
			if (auth == null) {
				auth = new WechatAuth();
				auth.setOpenId(openId);
				personInfo = WeiXinUserUtil.getPersonInfoFromRequest(user);
				personInfo.setShopOwnerFlag(1);
				auth.setPersonInfo(personInfo);
				we = WechatAuthService.register(auth, null);
				if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
					return null;
				}
			}
			personInfo = personInfoService.getPersonInfoById(auth.getUserId());
			request.getSession().setAttribute("user", personInfo);
			ShopExecution se = shopService.getByEmployeeId(personInfo
					.getUserId());
			request.getSession().setAttribute("user", personInfo);
			if (se.getShopList() == null || se.getShopList().size() <= 0) {
				return "shop/registershop";
			} else {
				request.getSession().setAttribute("shopList", se.getShopList());
				return "shop/shoplist";
			}
		}
		return null;
	}
}
