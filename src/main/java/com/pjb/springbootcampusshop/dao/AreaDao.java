package com.pjb.springbootcampusshop.dao;

import com.pjb.springbootcampusshop.entity.Area;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface AreaDao {
	/**
	 * 列出地域列表
	 */
	List<Area> queryArea();

	int insertArea(Area area);

	int updateArea(Area area);

	int deleteArea(long areaId);

	int batchDeleteArea(List<Long> areaIdList);
}
