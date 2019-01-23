package com.pjb.springbootcampusshop.service.impl;

import com.pjb.springbootcampusshop.dao.PersonInfoDao;
import com.pjb.springbootcampusshop.dao.WechatAuthDao;
import com.pjb.springbootcampusshop.dto.WechatAuthExecution;
import com.pjb.springbootcampusshop.entity.PersonInfo;
import com.pjb.springbootcampusshop.entity.WechatAuth;
import com.pjb.springbootcampusshop.enums.WechatAuthStateEnum;
import com.pjb.springbootcampusshop.service.WechatAuthService;
import com.pjb.springbootcampusshop.util.FileUtil;
import com.pjb.springbootcampusshop.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Date;

@Slf4j
@Service
public class WechatAuthServiceImpl implements WechatAuthService {
	private final WechatAuthDao wechatAuthDao;
	private final PersonInfoDao personInfoDao;

	@Autowired
	public WechatAuthServiceImpl(WechatAuthDao wechatAuthDao, PersonInfoDao personInfoDao) {
		this.wechatAuthDao = wechatAuthDao;
		this.personInfoDao = personInfoDao;
	}

	@Override
	public WechatAuth getWechatAuthByOpenId(String openId) {
		return wechatAuthDao.queryWechatInfoByOpenId(openId);
	}

	@Override
	@Transactional
	public WechatAuthExecution register(WechatAuth wechatAuth, CommonsMultipartFile profileImg){
		if (wechatAuth == null || wechatAuth.getOpenId() == null) {
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			wechatAuth.setCreateTime(new Date());
			if (wechatAuth.getPersonInfo() != null && wechatAuth.getPersonInfo().getUserId() == null) {
				if (profileImg != null) {
					try {
						addProfileImg(wechatAuth, profileImg);
					} catch (Exception e) {
						log.debug("addUserProfileImg error:" + e.toString());
						throw new RuntimeException("addUserProfileImg error: " + e.getMessage());
					}
				}
				try {
					wechatAuth.getPersonInfo().setCreateTime(new Date());
					wechatAuth.getPersonInfo().setLastEditTime(new Date());
					wechatAuth.getPersonInfo().setCustomerFlag(1);
					wechatAuth.getPersonInfo().setShopOwnerFlag(1);
					wechatAuth.getPersonInfo().setAdminFlag(0);
					wechatAuth.getPersonInfo().setEnableStatus(1);
					PersonInfo personInfo = wechatAuth.getPersonInfo();
					int effectedNum = personInfoDao.insertPersonInfo(personInfo);
					wechatAuth.setUserId(personInfo.getUserId());
					if (effectedNum <= 0) {
						throw new RuntimeException("添加用户信息失败");
					}
				} catch (Exception e) {
					log.debug("insertPersonInfo error:" + e.toString());
					throw new RuntimeException("insertPersonInfo error: " + e.getMessage());
				}
			}
			int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
			if (effectedNum <= 0) {
				throw new RuntimeException("帐号创建失败");
			} else {
				return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
			}
		} catch (Exception e) {
			log.debug("insertWechatAuth error:" + e.toString());
			throw new RuntimeException("insertWechatAuth error: " + e.getMessage());
		}
	}

	private void addProfileImg(WechatAuth wechatAuth, CommonsMultipartFile profileImg) {
		String dest = FileUtil.getPersonInfoImagePath();
		String profileImgAddr = ImageUtil.generateThumbnail(profileImg, dest);
		wechatAuth.getPersonInfo().setProfileImg(profileImgAddr);
	}

}
