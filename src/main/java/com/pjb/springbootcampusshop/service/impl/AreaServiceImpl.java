package com.pjb.springbootcampusshop.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjb.springbootcampusshop.cache.JedisUtil;
import com.pjb.springbootcampusshop.dao.AreaDao;
import com.pjb.springbootcampusshop.dto.AreaExecution;
import com.pjb.springbootcampusshop.entity.Area;
import com.pjb.springbootcampusshop.enums.AreaStateEnum;
import com.pjb.springbootcampusshop.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
	private final JedisUtil.Strings jedisStrings;
	private final JedisUtil.Keys jedisKeys;
	private final AreaDao areaDao;

	private static String AREALISTKEY = "arealist";

	@Autowired
	public AreaServiceImpl(JedisUtil.Strings jedisStrings, JedisUtil.Keys jedisKeys, AreaDao areaDao) {
		this.jedisStrings = jedisStrings;
		this.jedisKeys = jedisKeys;
		this.areaDao = areaDao;
	}

	@Override
	public List<Area> getAreaList() throws IOException {
		String key = AREALISTKEY;
		List<Area> areaList;
		ObjectMapper mapper = new ObjectMapper();
		if (!jedisKeys.exists(key)) {
			areaList = areaDao.queryArea();
			String jsonString = mapper.writeValueAsString(areaList);
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
			areaList = mapper.readValue(jsonString, javaType);
		}
		return areaList;
	}

	@Override
	@Transactional
	public AreaExecution addArea(Area area) {
		if (area.getAreaName() != null && !"".equals(area.getAreaName())) {
			area.setCreateTime(new Date());
			area.setLastEditTime(new Date());
			try {
				int effectedNum = areaDao.insertArea(area);
				return getAreaExecution(area, effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("添加区域信息失败:" + e.toString());
			}
		} else {
			return new AreaExecution(AreaStateEnum.EMPTY);
		}
	}

	private AreaExecution getAreaExecution(Area area, int effectedNum) {
		if (effectedNum > 0) {
			String key = AREALISTKEY;
			if (jedisKeys.exists(key)) {
				jedisKeys.del(key);
			}
			return new AreaExecution(AreaStateEnum.SUCCESS, area);
		} else {
			return new AreaExecution(AreaStateEnum.INNER_ERROR);
		}
	}

	@Override
	@Transactional
	public AreaExecution modifyArea(Area area) {
		if (area.getAreaId() != null && area.getAreaId() > 0) {
			area.setLastEditTime(new Date());
			try {
				int effectedNum = areaDao.updateArea(area);
				return getAreaExecution(area, effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("更新区域信息失败:" + e.toString());
			}
		} else {
			return new AreaExecution(AreaStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	public AreaExecution removeArea(long areaId) {
		if (areaId > 0) {
			try {
				int effectedNum = areaDao.deleteArea(areaId);
				return getAreaExecution(effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("删除区域信息失败:" + e.toString());
			}
		} else {
			return new AreaExecution(AreaStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	public AreaExecution removeAreaList(List<Long> areaIdList) {
		if (!areaIdList.isEmpty()) {
			try {
				int effectedNum = areaDao.batchDeleteArea(areaIdList);
				return getAreaExecution(effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("删除区域信息失败:" + e.toString());
			}
		} else {
			return new AreaExecution(AreaStateEnum.EMPTY);
		}
	}

	private AreaExecution getAreaExecution(int effectedNum) {
		if (effectedNum > 0) {
			String key = AREALISTKEY;
			if (jedisKeys.exists(key)) {
				jedisKeys.del(key);
			}
			return new AreaExecution(AreaStateEnum.SUCCESS);
		} else {
			return new AreaExecution(AreaStateEnum.INNER_ERROR);
		}
	}
}
