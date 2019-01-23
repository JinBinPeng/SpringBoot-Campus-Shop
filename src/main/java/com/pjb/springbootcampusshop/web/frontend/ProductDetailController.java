package com.pjb.springbootcampusshop.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.pjb.springbootcampusshop.entity.PersonInfo;
import com.pjb.springbootcampusshop.entity.Product;
import com.pjb.springbootcampusshop.service.ProductService;
import com.pjb.springbootcampusshop.util.HttpServletRequestUtil;
import com.pjb.springbootcampusshop.util.QRCodeUtil;
import com.pjb.springbootcampusshop.util.baidu.ShortNetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class ProductDetailController {
	@Autowired
	private ProductService productService;

	private static final String URLPREFIX = "https://open.weixin.qq.com/connect/oauth2/authorize?"
			+ "appid=wxd7f6c5b8899fba83&"
			+ "redirect_uri=115.28.159.6/shop/adduserproductmap&"
			+ "response_type=code&scope=snsapi_userinfo&state=";
	private static final String URLSUFFIX = "#wechat_redirect";

	@GetMapping("/listproductdetailpageinfo")
	private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		Product product = null;
		if (productId != -1) {
			product = productService.getProductById(productId);
			modelMap.put("product", product);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}

	@GetMapping("/generateqrcode4product")
	private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		if (productId != -1 && user != null && user.getUserId() != null) {
			long timpStamp = System.currentTimeMillis();
			String content = "{\"productId\":" + productId + ",\"customerId\":"
					+ user.getUserId() + ",\"createTime\":" + timpStamp + "}";
			String longUrl = URLPREFIX + content + URLSUFFIX;
			String shortUrl = ShortNetAddress.getShortURL(longUrl);
			BitMatrix qRcodeImg = QRCodeUtil.generateQRCodeStream(shortUrl,
					response);
			try {
				MatrixToImageWriter.writeToStream(qRcodeImg, "png",
						response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
