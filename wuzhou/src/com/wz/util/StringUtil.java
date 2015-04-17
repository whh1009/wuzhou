package com.wz.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class StringUtil extends StringUtils {

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
//			return Integer.parseInt(str);
			return new Double(Double.parseDouble(str)).intValue();
		}
	}

	public static double StringToDouble(String str) {
		if (str == null || "".equals(str)) {
			return 0;
		} else {
			return new Double(Double.parseDouble(str)).intValue();
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
	 * 格式化数字，默认四位，不足补0
	 * 
	 * @param num
	 * @return
	 */
	public static String formatNum(int num) {
		if (num >= 1 && num <= 9) {
			return "000" + num;
		} else if (num >= 10 && num <= 99) {
			return "00" + num;
		} else if (num >= 100 && num <= 999) {
			return "0" + num;
		} else {
			return "" + num;
		}
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
	public static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}

	/**
	 * 获取汉语拼音
	 * 
	 * @param strs
	 *            祖国
	 * @return
	 */
	public static String getPinYin(String strs) {
		strs = ObjectToString(strs);
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 不带音调
		// format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK); // 带音调
		format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
		char[] ch = strs.trim().toCharArray();
		StringBuffer buffer = new StringBuffer("");
		try {
			for (int i = 0; i < ch.length; i++) {
				if (Character.toString(ch[i]).matches("[\u4e00-\u9fa5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(ch[i], format);
					buffer.append(temp[0]);
					buffer.append(" ");
				} else {
					buffer.append(Character.toString(ch[i]));
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public static String getUpEname(String name) {
		if(ObjectToString(name).length()==0) return "";
        char[] strs = name.toCharArray();
        String newname = null;
         if (strs.length == 2) {    //如果姓名只有两个字
              newname = toUpCase(getEname("" + strs[0])) + " " + toUpCase(getEname("" + strs[1]));
          } else if (strs.length == 3) {    //如果姓名有三个字
              newname = toUpCase(getEname("" + strs[0])) + " "+ toUpCase(getEname("" + strs[1] + strs[2]));
          } else if (strs.length == 4) {    //如果姓名有四个字
              newname = toUpCase(getEname("" + strs[0] + strs[1])) + " "+ toUpCase(getEname("" + strs[2] + strs[3]));
        } else {
               newname = toUpCase(getEname(name));
        }
        return newname;
   }

	// 将中文转换为英文
	private static String getEname(String name) {
		HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
		pyFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE); // 设置样式
		pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		try {
			return PinyinHelper.toHanyuPinyinString(name, pyFormat, "");
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	// 首字母大写
	private static String toUpCase(String str) {
		StringBuffer newstr = new StringBuffer();
		newstr.append((str.substring(0, 1)).toUpperCase()).append(str.substring(1, str.length()));
		return newstr.toString();
	}

	public static void main(String args[]) {
		String str = "1";
		System.out.println(StringToInt(str));
	}

}
