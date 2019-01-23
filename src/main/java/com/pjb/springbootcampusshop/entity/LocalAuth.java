package com.pjb.springbootcampusshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalAuth{
	private Long localAuthId;
	private String userName;
	private String password;
	private Long userId;
	private Date createTime;
	private Date lastEditTime;
	private PersonInfo personInfo;

}
