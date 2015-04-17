package com.wz.common;

import java.util.HashMap;
import java.util.Map;

import com.wz.util.StringUtil;

public class BookLanguageMap {
	
	/**
	 * 初始化亚马逊 语种--代码
	 * @return
	 */
	private static Map<String, String> initAmazonMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("001--英文", "ENG");
		map.put("002--西文", "SPA");
		map.put("003--中文", "CHI");
		map.put("004--法文", "FRE");
		map.put("005--德语", "DEU");
		map.put("006--阿语", "ARA");
		map.put("007--俄语", "RUS");
		map.put("008--土文", "TUR");
		map.put("009--日语", "JPN");
		map.put("010--韩语", "KOR");
		map.put("011--意大利语", "ITA");
		map.put("012--印尼文", "IND");
		map.put("013--哈萨克斯坦文", "KAZ");
		map.put("014--蒙文", "MON");
		map.put("015--藏文", "TIB");
		map.put("016--波斯文", "PER");
		map.put("500--双语对应", "");
		return map;
	}
	
	/**
	 * 初始化 OverDrive 语种--代码
	 * @return
	 */
	private static Map<String, String> initOverDriveMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("001--英文", "en - English");
		map.put("002--西文", "es - Spanish");
		map.put("003--中文", "zh - Chinese");
		map.put("004--法文", "fr - French");
		map.put("005--德语", "de - German");
		map.put("006--阿语", "ar - Arabic");
		map.put("007--俄语", "ru - Russian");
		map.put("008--土文", "tr - Turkish");
		map.put("009--日语", "ja - Japanese");
		map.put("010--韩语", "");
		map.put("011--意大利语", "it - Italian");
		map.put("012--印尼文", "in - Indonesian");
		map.put("013--哈萨克斯坦文", "kk - Kazakh");
		map.put("014--蒙文", "mn - Mongolian");
		map.put("015--藏文", "bo - Tibetan");
		map.put("016--波斯文", "fa - Persian");
		map.put("500--双语对应", "");
		return map;
	}
	
	/**
	 * 获取亚马逊文种
	 * @param bookLanguage
	 * @return
	 */
	public static String getAmazonLanguage(String bookLanguage) {
		bookLanguage = StringUtil.ObjectToString(bookLanguage);
		if("".equals(bookLanguage)) return "";
		Map<String, String> map = initAmazonMap();
		return map.get(bookLanguage);
	}
	/**
	 * 获取OverDrive 文种
	 */
	public static String getOverDriveLanguage(String bookLanguage) {
		bookLanguage = StringUtil.ObjectToString(bookLanguage);
		if("".equals(bookLanguage)) return "";
		Map<String, String> map = initOverDriveMap();
		return map.get(bookLanguage);
	}
	
}
