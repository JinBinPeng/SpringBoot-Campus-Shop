package com.pjb.springbootcampusshop.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.pjb.springbootcampusshop.entity.UserShopMap;
import org.springframework.stereotype.Component;

@Component
public interface UserShopMapDao {

	List<UserShopMap> queryUserShopMapList(@Param("userShopCondition") UserShopMap userShopCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	UserShopMap queryUserShopMap(@Param("userId") long userId, @Param("shopId") long shopId);

	int queryUserShopMapCount(@Param("userShopCondition") UserShopMap userShopCondition);

	int insertUserShopMap(UserShopMap userShopMap);

	int updateUserShopMapPoint(UserShopMap userShopMap);

}
