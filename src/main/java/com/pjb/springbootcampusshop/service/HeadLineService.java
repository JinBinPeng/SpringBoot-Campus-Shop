package com.pjb.springbootcampusshop.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.pjb.springbootcampusshop.dto.HeadLineExecution;
import com.pjb.springbootcampusshop.entity.HeadLine;

public interface HeadLineService {

	List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;

	HeadLineExecution addHeadLine(HeadLine headLine, CommonsMultipartFile thumbnail);

	HeadLineExecution modifyHeadLine(HeadLine headLine, CommonsMultipartFile thumbnail);

	HeadLineExecution removeHeadLine(long headLineId);

	HeadLineExecution removeHeadLineList(List<Long> headLineIdList);

}
