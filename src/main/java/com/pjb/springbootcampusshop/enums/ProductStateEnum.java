package com.pjb.springbootcampusshop.enums;

public enum ProductStateEnum {
	OFFLINE(-1, "非法商品"), SUCCESS(0, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(
			-1001, "操作失败"),EMPTY(-1002, "商品为空");

	private int state;

	private String stateInfo;

	private ProductStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static ProductStateEnum stateOf(int index) {
		for (ProductStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}
