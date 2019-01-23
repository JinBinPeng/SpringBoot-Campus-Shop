package com.pjb.springbootcampusshop.dao;

import java.util.List;

import com.pjb.springbootcampusshop.entity.ProductImg;
import org.springframework.stereotype.Component;

@Component
public interface ProductImgDao {

	List<ProductImg> queryProductImgList(long productId);

	int batchInsertProductImg(List<ProductImg> productImgList);

	int deleteProductImgByProductId(long productId);
}
