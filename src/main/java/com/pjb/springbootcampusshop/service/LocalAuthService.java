package com.pjb.springbootcampusshop.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.pjb.springbootcampusshop.dto.LocalAuthExecution;
import com.pjb.springbootcampusshop.entity.LocalAuth;

public interface LocalAuthService {

	LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password);

	LocalAuth getLocalAuthByUserId(long userId);

	LocalAuthExecution register(LocalAuth localAuth, CommonsMultipartFile profileImg) ;

	LocalAuthExecution bindLocalAuth(LocalAuth localAuth);

	LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword);
}
