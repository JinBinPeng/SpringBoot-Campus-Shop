package com.pjb.springbootcampusshop.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjb.springbootcampusshop.cache.JedisUtil;
import com.pjb.springbootcampusshop.dao.HeadLineDao;
import com.pjb.springbootcampusshop.dto.HeadLineExecution;
import com.pjb.springbootcampusshop.entity.HeadLine;
import com.pjb.springbootcampusshop.enums.HeadLineStateEnum;
import com.pjb.springbootcampusshop.service.HeadLineService;
import com.pjb.springbootcampusshop.util.FileUtil;
import com.pjb.springbootcampusshop.util.ImageUtil;

@Service
@Transactional
public class HeadLineServiceImpl implements HeadLineService {
	private final JedisUtil.Strings jedisStrings;
	private final JedisUtil.Keys jedisKeys;
	private final HeadLineDao headLineDao;
	private static String HLLISTKEY = "headlinelist";

	@Autowired
	public HeadLineServiceImpl(JedisUtil.Strings jedisStrings, JedisUtil.Keys jedisKeys, HeadLineDao headLineDao) {
		this.jedisStrings = jedisStrings;
		this.jedisKeys = jedisKeys;
		this.headLineDao = headLineDao;
	}

	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
		List<HeadLine> headLineList;
		ObjectMapper mapper = new ObjectMapper();
		String key = HLLISTKEY;
		if (headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		if (!jedisKeys.exists(key)) {
			headLineList = headLineDao.queryHeadLine(headLineCondition);
			String jsonString = mapper.writeValueAsString(headLineList);
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
			headLineList = mapper.readValue(jsonString, javaType);
		}
		return headLineList;
	}

	@Override
	public HeadLineExecution addHeadLine(HeadLine headLine, CommonsMultipartFile thumbnail) {
		if (headLine != null) {
			headLine.setCreateTime(new Date());
			headLine.setLastEditTime(new Date());
			if (thumbnail != null) {
				addThumbnail(headLine, thumbnail);
			}
			try {
				int effectedNum = headLineDao.insertHeadLine(headLine);
				return getHeadLineExecution(headLine, effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("添加区域信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	private HeadLineExecution getHeadLineExecution(HeadLine headLine, int effectedNum) {
		if (effectedNum > 0) {
			String prefix = HLLISTKEY;
			Set<String> keySet = jedisKeys.keys(prefix + "*");
			for (String key : keySet) {
				jedisKeys.del(key);
			}
			return new HeadLineExecution(HeadLineStateEnum.SUCCESS,
					headLine);
		} else {
			return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
		}
	}

	@Override
	public HeadLineExecution modifyHeadLine(HeadLine headLine, CommonsMultipartFile thumbnail) {
		if (headLine.getLineId() != null && headLine.getLineId() > 0) {
			headLine.setLastEditTime(new Date());
			if (thumbnail != null) {
				HeadLine tempHeadLine = headLineDao.queryHeadLineById(headLine.getLineId());
				if (tempHeadLine.getLineImg() != null) {
					FileUtil.deleteFile(tempHeadLine.getLineImg());
				}
				addThumbnail(headLine, thumbnail);
			}
			try {
				int effectedNum = headLineDao.updateHeadLine(headLine);
				return getHeadLineExecution(headLine, effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("更新头条信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	@Override
	public HeadLineExecution removeHeadLine(long headLineId) {
		if (headLineId > 0) {
			try {
				HeadLine tempHeadLine = headLineDao
						.queryHeadLineById(headLineId);
				if (tempHeadLine.getLineImg() != null) {
					FileUtil.deleteFile(tempHeadLine.getLineImg());
				}
				int effectedNum = headLineDao.deleteHeadLine(headLineId);
				return getHeadLineExecution(effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("删除头条信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	private HeadLineExecution getHeadLineExecution(int effectedNum) {
		if (effectedNum > 0) {
			String prefix = HLLISTKEY;
			Set<String> keySet = jedisKeys.keys(prefix + "*");
			for (String key : keySet) {
				jedisKeys.del(key);
			}
			return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
		} else {
			return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
		}
	}

	@Override
	public HeadLineExecution removeHeadLineList(List<Long> headLineIdList) {
		if (!headLineIdList.isEmpty()) {
			try {
				List<HeadLine> headLineList = headLineDao.queryHeadLineByIds(headLineIdList);
				for (HeadLine headLine : headLineList) {
					if (headLine.getLineImg() != null) {
						FileUtil.deleteFile(headLine.getLineImg());
					}
				}
				int effectedNum = headLineDao.batchDeleteHeadLine(headLineIdList);
				return getHeadLineExecution(effectedNum);
			} catch (Exception e) {
				throw new RuntimeException("删除头条信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	private void addThumbnail(HeadLine headLine, CommonsMultipartFile thumbnail) {
		String dest = FileUtil.getHeadLineImagePath();
		String thumbnailAddr = ImageUtil.generateNormalImg(thumbnail, dest);
		headLine.setLineImg(thumbnailAddr);
	}

}
