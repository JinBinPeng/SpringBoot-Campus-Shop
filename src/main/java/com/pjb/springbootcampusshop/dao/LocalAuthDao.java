package com.pjb.springbootcampusshop.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.pjb.springbootcampusshop.entity.LocalAuth;
import org.springframework.stereotype.Component;

@Component
public interface LocalAuthDao {

	LocalAuth queryLocalByUserNameAndPwd(@Param("userName") String userName, @Param("password") String password);

	LocalAuth queryLocalByUserId(@Param("userId") long userId);

	int insertLocalAuth(LocalAuth localAuth);

	int updateLocalAuth(@Param("userId") Long userId,
                        @Param("userName") String userName,
                        @Param("password") String password,
                        @Param("newPassword") String newPassword,
                        @Param("lastEditTime") Date lastEditTime);
}
