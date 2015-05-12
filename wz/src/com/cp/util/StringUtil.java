package com.cp.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class StringUtil {

	/**
	 * 字符串转数字 如果字符串是空，返回0
	 * 
	 * @param str
	 * @return
	 */
	public static int StringToInt(String str) {
		if (str == null || "".equals(str)) {
			return 0;
		} else {
			return Integer.parseInt(str);
		}
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static long StringToLong(String str) {
		if (str == null || "".equals(str)) {
			return 0;
		} else {
			return Long.parseLong(str);
		}
	}

	/**
	 * Object转字符串 Object==null 返回“”
	 * 
	 * @param obj
	 * @return
	 */
	public static String ObjectToString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString().trim();
		}
	}

	/**
	 * 当前时间转字符串 注意月份一定要大写（MM）
	 * 
	 * @format [yyyy-MM-dd
	 *         hh:mm:ss],[yyyy-MM-dd],[yyyyMMdd],[yyyyMMddhhmmss]等等……
	 * @return
	 */
	public static String dateToString(String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}

	/**
	 * 检查字符串是否为空 为空返回 true，有值返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null) {
			return true;
		} else if (str.trim().length() < 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 忽略首尾逗号
	 * 
	 * @param str
	 * @return
	 */
	public static String ignoreComma(String str) {
		if (str == null || "".equals(str)) {
			return "";
		} else {
			return str.replaceAll("(^[,]*|[,]*$|(?<=[,])[,]+)", "");
		}
	}

	/**
	 * 打印map
	 * 
	 * @param map
	 */
	public static void print(Map<String, String> map) {
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			String key1 = it.next().getKey();
			System.out.println(key1 + "的value是：" + map.get(key1));
		}
	}

	/**
	 * MD5 32位加密
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static String MD5(String str) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("utf-8"));
			for (byte b : md.digest())
				sb.append(Integer.toString(b >>> 4 & 0xF, 16)).append(Integer.toString(b & 0xF, 16));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
