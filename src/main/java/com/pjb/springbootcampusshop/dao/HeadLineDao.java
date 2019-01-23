package com.pjb.springbootcampusshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pjb.springbootcampusshop.entity.HeadLine;
import org.springframework.stereotype.Component;

@Component
public interface HeadLineDao {

	List<HeadLine> queryHeadLine(HeadLine headLine);

	HeadLine queryHeadLineById(long lineId);

	List<HeadLine> queryHeadLineByIds(List<Long> lineIdList);

	int insertHeadLine(HeadLine headLine);

	int updateHeadLine(HeadLine headLine);

	int deleteHeadLine(long headLineId);

	int batchDeleteHeadLine(List<Long> lineIdList);
}
