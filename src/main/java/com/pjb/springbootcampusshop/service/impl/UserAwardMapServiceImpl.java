package com.pjb.springbootcampusshop.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pjb.springbootcampusshop.dao.UserAwardMapDao;
import com.pjb.springbootcampusshop.dao.UserShopMapDao;
import com.pjb.springbootcampusshop.dto.UserAwardMapExecution;
import com.pjb.springbootcampusshop.entity.UserAwardMap;
import com.pjb.springbootcampusshop.entity.UserShopMap;
import com.pjb.springbootcampusshop.enums.UserAwardMapStateEnum;
import com.pjb.springbootcampusshop.service.UserAwardMapService;
import com.pjb.springbootcampusshop.util.PageCalculator;

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {
	private final UserAwardMapDao userAwardMapDao;
	private final UserShopMapDao userShopMapDao;

	@Autowired
	public UserAwardMapServiceImpl(UserAwardMapDao userAwardMapDao, UserShopMapDao userShopMapDao) {
		this.userAwardMapDao = userAwardMapDao;
		this.userShopMapDao = userShopMapDao;
	}

	@Override
	public UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize) {
		if (userAwardCondition != null && pageIndex != null && pageSize != null) {
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardCondition, beginIndex, pageSize);
			int count = userAwardMapDao.queryUserAwardMapCount(userAwardCondition);
			UserAwardMapExecution userAwardMapExecution = new UserAwardMapExecution();
			userAwardMapExecution.setUserAwardMapList(userAwardMapList);
			userAwardMapExecution.setCount(count);
			return userAwardMapExecution;
		} else {
			return null;
		}

	}

	@Override
	public UserAwardMap getUserAwardMapById(long userAwardMapId) {
		return userAwardMapDao.queryUserAwardMapById(userAwardMapId);
	}

	@Override
	@Transactional
	public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) {
		if (userAwardMap != null && userAwardMap.getUserId() != null && userAwardMap.getShopId() != null) {
			userAwardMap.setCreateTime(new Date());
			try {
				int effectedNum;
				if (userAwardMap.getPoint() != null && userAwardMap.getPoint() > 0) {
					UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userAwardMap.getUserId(), userAwardMap.getShopId());
					if (userShopMap != null) {
						if (userShopMap.getPoint() >= userAwardMap.getPoint()) {
							userShopMap.setPoint(userShopMap.getPoint() - userAwardMap.getPoint());
							effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
							if (effectedNum <= 0) {
								throw new RuntimeException("更新积分信息失败");
							}
						} else {
							throw new RuntimeException("积分不足无法领取");
						}

					} else {
						// 在店铺没有积分
						throw new RuntimeException("在本店铺没有积分，无法对换奖品");
					}
				}
				effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
				if (effectedNum <= 0) {
					throw new RuntimeException("领取奖励失败");
				}

				return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
			} catch (Exception e) {
				throw new RuntimeException("领取奖励失败:" + e.toString());
			}
		} else {
			return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_INFO);
		}
	}

	@Override
	@Transactional
	public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) {
		if (userAwardMap == null || userAwardMap.getUserAwardId() == null || userAwardMap.getUsedStatus() == null) {
			return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_ID);
		} else {
			try {
				int effectedNum = userAwardMapDao.updateUserAwardMap(userAwardMap);
				if (effectedNum <= 0) {
					return new UserAwardMapExecution(UserAwardMapStateEnum.INNER_ERROR);
				} else {
					return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
				}
			} catch (Exception e) {
				throw new RuntimeException("modifyUserAwardMap error: " + e.getMessage());
			}
		}
	}

}
