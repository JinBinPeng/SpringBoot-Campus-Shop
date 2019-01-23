package com.pjb.springbootcampusshop.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.pjb.springbootcampusshop.dto.WechatAuthExecution;
import com.pjb.springbootcampusshop.entity.WechatAuth;

public interface WechatAuthService {

	WechatAuth getWechatAuthByOpenId(String openId);

	WechatAuthExecution register(WechatAuth wechatAuth, CommonsMultipartFile profileImg) ;

}
