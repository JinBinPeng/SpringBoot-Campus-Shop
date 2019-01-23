package com.pjb.springbootcampusshop.util;

import java.util.EnumMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QRCodeUtil {
	public static BitMatrix generateQRCodeStream(String url,
			HttpServletResponse resp) {
		resp.setHeader("Cache-Control", "no-store");
		resp.setHeader("Pragma", "no-cache");
		resp.setDateHeader("Expires", 0);
		resp.setContentType("image/png");
		Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 0);
		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode("https://www.baidu.com",
					BarcodeFormat.QR_CODE, 300, 300, hints);
		} catch (WriterException e) {
			log.warn(e.toString());
			return null;
		}
		return bitMatrix;
	}
}
