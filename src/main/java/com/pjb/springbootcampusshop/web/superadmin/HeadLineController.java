package com.pjb.springbootcampusshop.web.superadmin;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjb.springbootcampusshop.dto.HeadLineExecution;
import com.pjb.springbootcampusshop.entity.ConstantForSuperAdmin;
import com.pjb.springbootcampusshop.entity.HeadLine;
import com.pjb.springbootcampusshop.enums.HeadLineStateEnum;
import com.pjb.springbootcampusshop.service.HeadLineService;
import com.pjb.springbootcampusshop.util.HttpServletRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/superadmin")
public class HeadLineController {
	private final HeadLineService headLineService;

	@Autowired
	public HeadLineController(HeadLineService headLineService) {
		this.headLineService = headLineService;
	}

	@PostMapping("/listheadlines")
	private Map<String, Object> listHeadLines(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		List<HeadLine> list;
		try {
			Integer enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
			HeadLine headLine = new HeadLine();
			if (enableStatus > -1) {
				headLine.setEnableStatus(enableStatus);
			}
			list = headLineService.getHeadLineList(headLine);
			modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, list);
			modelMap.put(ConstantForSuperAdmin.TOTAL, list.size());
		} catch (Exception e) {
			log.warn(e.toString());
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@PostMapping("/addheadline")
	private Map<String, Object> addHeadLine(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		HeadLine headLine;
		String headLineStr = HttpServletRequestUtil.getString(request, "headLineStr");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile thumbnail = (CommonsMultipartFile) multipartRequest.getFile("headTitleManagementAdd_lineImg");
		try {
			headLine = mapper.readValue(headLineStr, HeadLine.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (headLine != null && thumbnail != null) {
			try {
				// decode可能有中文的地方
				headLine.setLineName((headLine.getLineName() == null) ? null : URLDecoder.decode(headLine.getLineName(), "UTF-8"));
				HeadLineExecution ae = headLineService.addHeadLine(headLine, thumbnail);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}

	@PostMapping("/modifyheadline")
	private Map<String, Object> modifyHeadLine(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		HeadLine headLine;
		String headLineStr = HttpServletRequestUtil.getString(request, "headLineStr");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile thumbnail = (CommonsMultipartFile) multipartRequest.getFile("headTitleManagementEdit_lineImg");
		try {
			headLine = mapper.readValue(headLineStr, HeadLine.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (headLine != null && headLine.getLineId() != null) {
			try {
				// decode可能有中文的地方
				headLine.setLineName((headLine.getLineName() == null) ? null : URLDecoder.decode(headLine.getLineName(), "UTF-8"));
				HeadLineExecution headLineExecution = headLineService.modifyHeadLine(headLine, thumbnail);
				if (headLineExecution.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", headLineExecution.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}

	@PostMapping("/removeheadline")
	private Map<String, Object> removeHeadLine(Long headLineId) {
		Map<String, Object> modelMap = new HashMap<>();
		if (headLineId != null && headLineId > 0) {
			try {
				HeadLineExecution headLineExecution = headLineService.removeHeadLine(headLineId);
				if (headLineExecution.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", headLineExecution.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}

	@PostMapping("/removeheadlines")
	private Map<String, Object> removeHeadLines(String headLineIdListStr) {
		Map<String, Object> modelMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(
				ArrayList.class, Long.class);
		List<Long> headLineIdList = null;
		try {
			headLineIdList = mapper.readValue(headLineIdListStr, javaType);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		if (headLineIdList != null && headLineIdList.size() > 0) {
			try {
				HeadLineExecution ae = headLineService.removeHeadLineList(headLineIdList);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入区域信息");
		}
		return modelMap;
	}

}
