package com.pjb.springbootcampusshop.web.superadmin;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pjb.springbootcampusshop.dto.PersonInfoExecution;
import com.pjb.springbootcampusshop.entity.ConstantForSuperAdmin;
import com.pjb.springbootcampusshop.entity.PersonInfo;
import com.pjb.springbootcampusshop.enums.PersonInfoStateEnum;
import com.pjb.springbootcampusshop.service.PersonInfoService;
import com.pjb.springbootcampusshop.util.HttpServletRequestUtil;

@RestController
@RequestMapping("/superadmin")
public class PersonInfoController {
	private final PersonInfoService personInfoService;

	@Autowired
	public PersonInfoController(PersonInfoService personInfoService) {
		this.personInfoService = personInfoService;
	}

	@PostMapping("/listpersonInfos")
	private Map<String, Object> listPersonInfos(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		PersonInfoExecution personInfos;
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		if (pageIndex > 0 && pageSize > 0) {
			try {
				PersonInfo personInfo = new PersonInfo();
				int enableStatus = HttpServletRequestUtil.getInt(request,
						"enableStatus");
				if (enableStatus > -1) {
					personInfo.setEnableStatus(enableStatus);
				}
				String name = HttpServletRequestUtil.getString(request, "name");
				if (name != null) {
					personInfo.setName(URLDecoder.decode(name, "UTF-8"));
				}
				personInfos = personInfoService.getPersonInfoList(personInfo,
						pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (personInfos.getPersonInfoList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE,
						personInfos.getPersonInfoList());
				modelMap.put(ConstantForSuperAdmin.TOTAL,
						personInfos.getCount());
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE,
						new ArrayList<PersonInfo>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}

	@PostMapping("/modifypersonInfo")
	private Map<String, Object> modifyPersonInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		long userId = HttpServletRequestUtil.getLong(request, "userId");
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		if (userId > 0 && enableStatus > 0) {
			try {
				PersonInfo personInfo = new PersonInfo();
				personInfo.setUserId(userId);
				personInfo.setEnableStatus(enableStatus);
				PersonInfoExecution ae = personInfoService.modifyPersonInfo(personInfo);
				if (ae.getState() == PersonInfoStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入需要修改的帐号信息");
		}
		return modelMap;
	}

}
