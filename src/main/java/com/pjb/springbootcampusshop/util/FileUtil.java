package com.pjb.springbootcampusshop.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileUtil {
	private static String seperator = System.getProperty("file.separator");
	private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); // 时间格式化的格式

	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath;
		if (os.toLowerCase().startsWith("win")) {
			basePath = "D:/projectdev/image/";
		} else {
			basePath = "/home/xiangzepro/";
		}
		return basePath.replace("/", seperator);
	}

	public static String getHeadLineImagePath() {
		return "../upload/images/item/headtitle/".replace("/", seperator);
	}

	public static String getShopCategoryImagePath() {
		return "../upload/images/item/shopcategory/".replace("/", seperator);
	}
	
	public static String getPersonInfoImagePath() {
		return "../upload/images/item/personinfo/".replace("/", seperator);
	}

	public static String getShopImagePath(long shopId) {
		String shopImagePathBuilder = "../upload/images/item/shop/" + shopId ;
		return shopImagePathBuilder.replace("/", seperator);
	}

	public static String getRandomFileName() {
		// 生成随机文件名：当前年月日时分秒+五位随机数（为了在实际项目中防止文件同名而进行的处理）
		int rannum =new Random().nextInt(99999 - 10000 + 1)+10000; //获取随机数
		String nowTimeStr = sDateFormat.format(new Date()); // 当前时间
		return nowTimeStr + rannum;
	}

	public static void deleteFile(String storePath) {
		File file = new File(getImgBasePath() + storePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File file1 : files != null ? files : new File[0]) {
					file1.delete();
				}
			}
			file.delete();
		}
	}
}
