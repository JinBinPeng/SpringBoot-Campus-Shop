package com.pjb.springbootcampusshop.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pjb.springbootcampusshop.dao.PersonInfoDao;
import com.pjb.springbootcampusshop.dao.ShopDao;
import com.pjb.springbootcampusshop.dao.UserProductMapDao;
import com.pjb.springbootcampusshop.dao.UserShopMapDao;
import com.pjb.springbootcampusshop.dto.UserProductMapExecution;
import com.pjb.springbootcampusshop.entity.PersonInfo;
import com.pjb.springbootcampusshop.entity.Shop;
import com.pjb.springbootcampusshop.entity.UserProductMap;
import com.pjb.springbootcampusshop.entity.UserShopMap;
import com.pjb.springbootcampusshop.enums.UserProductMapStateEnum;
import com.pjb.springbootcampusshop.service.UserProductMapService;
import com.pjb.springbootcampusshop.util.PageCalculator;

@Service
public class UserProductMapServiceImpl implements UserProductMapService {
    private final UserProductMapDao userProductMapDao;
    private final UserShopMapDao userShopMapDao;
    private final PersonInfoDao personInfoDao;
    private final ShopDao shopDao;

    @Autowired
    public UserProductMapServiceImpl(UserProductMapDao userProductMapDao, UserShopMapDao userShopMapDao, PersonInfoDao personInfoDao, ShopDao shopDao) {
        this.userProductMapDao = userProductMapDao;
        this.userShopMapDao = userShopMapDao;
        this.personInfoDao = personInfoDao;
        this.shopDao = shopDao;
    }

    @Override
    public UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex, Integer pageSize) {
        if (userProductCondition != null && pageIndex != null && pageSize != null) {
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductCondition, beginIndex, pageSize);
            int count = userProductMapDao.queryUserProductMapCount(userProductCondition);
            UserProductMapExecution userProductMapExecution = new UserProductMapExecution();
            userProductMapExecution.setUserProductMapList(userProductMapList);
            userProductMapExecution.setCount(count);
            return userProductMapExecution;
        } else {
            return null;
        }

    }

    @Override
    @Transactional
    public UserProductMapExecution addUserProductMap(UserProductMap userProductMap) {
        if (userProductMap != null && userProductMap.getUserId() != null && userProductMap.getShopId() != null) {
            userProductMap.setCreateTime(new Date());
            try {
                int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
                if (effectedNum <= 0) {
                    throw new RuntimeException("添加消费记录失败");
                }
                if (userProductMap.getPoint() != null && userProductMap.getPoint() > 0) {
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userProductMap.getUserId(), userProductMap.getShopId());
                    if (userShopMap != null) {
                        if (userShopMap.getPoint() >= userProductMap.getPoint()) {
                            userShopMap.setPoint(userShopMap.getPoint() + userProductMap.getPoint());
                            effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
                            if (effectedNum <= 0) {
                                throw new RuntimeException("更新积分信息失败");
                            }
                        }

                    } else {
                        // 在店铺没有过消费记录，添加一条积分信息
                        userShopMap = compactUserShopMap4Add(userProductMap.getUserId(), userProductMap.getShopId(), userProductMap.getPoint());
                        effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
                        if (effectedNum <= 0) {
                            throw new RuntimeException("积分信息创建失败");
                        }
                    }
                }
                return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS, userProductMap);
            } catch (Exception e) {
                throw new RuntimeException("添加授权失败:" + e.toString());
            }
        } else {
            return new UserProductMapExecution(UserProductMapStateEnum.NULL_USERPRODUCT_INFO);
        }
    }

    private UserShopMap compactUserShopMap4Add(Long userId, Long shopId, Integer point) {
        UserShopMap userShopMap = null;
        if (userId != null && shopId != null) {
            userShopMap = new UserShopMap();
            PersonInfo personInfo = personInfoDao.queryPersonInfoById(userId);
            Shop shop = shopDao.queryByShopId(shopId);
            userShopMap.setUserId(userId);
            userShopMap.setShopId(shopId);
            userShopMap.setUserName(personInfo.getName());
            userShopMap.setShopName(shop.getShopName());
            userShopMap.setCreateTime(new Date());
            userShopMap.setPoint(point);
        }
        return userShopMap;
    }

}
