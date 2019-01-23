package com.pjb.springbootcampusshop.util;

public class PageCalculator {
	private PageCalculator(){

	}
	public static int calculatePageCount(int totalCount, int pageSize) {
		int idealPage = totalCount / pageSize;
		return (totalCount % pageSize == 0) ? idealPage : (idealPage + 1);
	}

	public static int calculateRowIndex(int pageIndex, int pageSize) {
		return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
	}
}
