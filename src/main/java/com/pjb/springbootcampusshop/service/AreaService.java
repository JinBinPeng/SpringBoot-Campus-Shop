package com.pjb.springbootcampusshop.service;

import java.io.IOException;
import java.util.List;

import com.pjb.springbootcampusshop.dto.AreaExecution;
import com.pjb.springbootcampusshop.entity.Area;

public interface AreaService {

	List<Area> getAreaList() throws IOException;

	AreaExecution addArea(Area area);

	AreaExecution modifyArea(Area area);

	AreaExecution removeArea(long areaId);

	AreaExecution removeAreaList(List<Long> areaIdList);

}
