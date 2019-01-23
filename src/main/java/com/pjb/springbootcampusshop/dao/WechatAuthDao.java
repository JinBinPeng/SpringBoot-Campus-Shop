package com.pjb.springbootcampusshop.dao;

import com.pjb.springbootcampusshop.entity.WechatAuth;
import org.springframework.stereotype.Component;

@Component
public interface WechatAuthDao {

	WechatAuth queryWechatInfoByOpenId(String openId);

	int insertWechatAuth(WechatAuth wechatAuth);

	int deleteWechatAuth(Long wechatAuthId);
}
