package com.pjb.springbootcampusshop.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.pjb.springbootcampusshop.dto.AwardExecution;
import com.pjb.springbootcampusshop.entity.Award;

public interface AwardService {

	AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize);

	Award getAwardById(long awardId);

	AwardExecution addAward(Award award, CommonsMultipartFile thumbnail);

	AwardExecution modifyAward(Award award, CommonsMultipartFile thumbnail);

}
