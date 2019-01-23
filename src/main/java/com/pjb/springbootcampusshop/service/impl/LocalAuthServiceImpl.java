package com.pjb.springbootcampusshop.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.pjb.springbootcampusshop.dao.LocalAuthDao;
import com.pjb.springbootcampusshop.dao.PersonInfoDao;
import com.pjb.springbootcampusshop.dto.LocalAuthExecution;
import com.pjb.springbootcampusshop.entity.LocalAuth;
import com.pjb.springbootcampusshop.entity.PersonInfo;
import com.pjb.springbootcampusshop.enums.LocalAuthStateEnum;
import com.pjb.springbootcampusshop.service.LocalAuthService;
import com.pjb.springbootcampusshop.util.FileUtil;
import com.pjb.springbootcampusshop.util.ImageUtil;
import com.pjb.springbootcampusshop.util.MD5;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

	private final LocalAuthDao localAuthDao;
	private final PersonInfoDao personInfoDao;

	@Autowired
	public LocalAuthServiceImpl(LocalAuthDao localAuthDao, PersonInfoDao personInfoDao) {
		this.localAuthDao = localAuthDao;
		this.personInfoDao = personInfoDao;
	}

	@Override
	public LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password) {
		return localAuthDao.queryLocalByUserNameAndPwd(userName, password);
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		return localAuthDao.queryLocalByUserId(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution register(LocalAuth localAuth, CommonsMultipartFile profileImg) {
		if (localAuth == null || localAuth.getPassword() == null || localAuth.getUserName() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			if (localAuth.getPersonInfo() != null && localAuth.getPersonInfo().getUserId() == null) {
				if (profileImg != null) {
					localAuth.getPersonInfo().setCreateTime(new Date());
					localAuth.getPersonInfo().setLastEditTime(new Date());
					localAuth.getPersonInfo().setEnableStatus(1);
					try {
						addProfileImg(localAuth, profileImg);
					} catch (Exception e) {
						throw new RuntimeException("addUserProfileImg error: " + e.getMessage());
					}
				}
				try {
					PersonInfo personInfo = localAuth.getPersonInfo();
					int effectedNum = personInfoDao.insertPersonInfo(personInfo);
					localAuth.setUserId(personInfo.getUserId());
					if (effectedNum <= 0) {
						throw new RuntimeException("添加用户信息失败");
					}
				} catch (Exception e) {
					throw new RuntimeException("insertPersonInfo error: " + e.getMessage());
				}
			}
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);
			if (effectedNum <= 0) {
				throw new RuntimeException("帐号创建失败");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
			}
		} catch (Exception e) {
			throw new RuntimeException("insertLocalAuth error: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) {
		if (localAuth == null || localAuth.getPassword() == null || localAuth.getUserName() == null || localAuth.getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getUserId());
		if (tempAuth != null) {
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		try {
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);
			if (effectedNum <= 0) {
				throw new RuntimeException("帐号绑定失败");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
			}
		} catch (Exception e) {
			throw new RuntimeException("insertLocalAuth error: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String userName,
			String password, String newPassword) {
		if (userId != null && userName != null && password != null
				&& newPassword != null && !password.equals(newPassword)) {
			try {
				int effectedNum = localAuthDao.updateLocalAuth(userId, userName, MD5.getMd5(password), MD5.getMd5(newPassword), new Date());
				if (effectedNum <= 0) {
					throw new RuntimeException("更新密码失败");
				}
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			} catch (Exception e) {
				throw new RuntimeException("更新密码失败:" + e.toString());
			}
		} else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}

	private void addProfileImg(LocalAuth localAuth, CommonsMultipartFile profileImg) {
		String dest = FileUtil.getPersonInfoImagePath();
		String profileImgAddr = ImageUtil.generateThumbnail(profileImg, dest);
		localAuth.getPersonInfo().setProfileImg(profileImgAddr);
	}
}
