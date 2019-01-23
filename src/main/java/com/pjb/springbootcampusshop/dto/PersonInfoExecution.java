package com.pjb.springbootcampusshop.dto;

import java.util.List;

import com.pjb.springbootcampusshop.entity.PersonInfo;
import com.pjb.springbootcampusshop.enums.PersonInfoStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装执行后结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfoExecution {

	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	// 店铺数量
	private int count;

	// 操作的personInfo（增删改店铺的时候用）
	private PersonInfo personInfo;

	// 获取的personInfo列表(查询店铺列表的时候用)
	private List<PersonInfo> personInfoList;

	// 失败的构造器
	public PersonInfoExecution(PersonInfoStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 成功的构造器
	public PersonInfoExecution(PersonInfoStateEnum stateEnum, PersonInfo personInfo) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.personInfo = personInfo;
	}
}