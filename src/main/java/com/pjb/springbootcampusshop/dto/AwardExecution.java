package com.pjb.springbootcampusshop.dto;

import java.util.List;

import com.pjb.springbootcampusshop.entity.Award;
import com.pjb.springbootcampusshop.enums.AwardStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	// 店铺数量
	private int count;

	// 操作的award（增删改商品的时候用）
	private Award award;

	// 获取的award列表(查询商品列表的时候用)
	private List<Award> awardList;


	// 失败的构造器
	public AwardExecution(AwardStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 成功的构造器
	public AwardExecution(AwardStateEnum stateEnum, Award award) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.award = award;
	}
}
