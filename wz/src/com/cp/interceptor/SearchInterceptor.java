package com.cp.interceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.cp.controller.SearchController;
import com.cp.util.StringUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
/**
 * 拦截器，拦截非法请求
 * 1. 过滤关键词，辞书ID范围
 * 2. 对检索内容MD5加密验证
 * 3. 同一IP，1秒内只能请求1次
 * @author wanghonghui
 *
 */
public class SearchInterceptor implements Interceptor {
	
	private static Logger log = Logger.getLogger("presentOperatorLog");
	
	public void intercept(ActionInvocation ai) {
		String keyWord = StringUtil.ObjectToString(ai.getController().getPara("keyWord"));
		String dictIds = StringUtil.ObjectToString(ai.getController().getPara("dictIds"));
		String ext = StringUtil.ObjectToString(ai.getController().getPara("ext"));
		if ("".equals(keyWord)) { // 关键词是空
			ai.getController().renderText("关键词是空");
			log.debug("检索失败，关键词是空");
			return;
		}
		if ("".equals(dictIds)) { // 辞书是空
			ai.getController().renderText("辞书是空");
			log.debug("检索失败，辞书范围是空");
			return;
		}
		if ("".equals(ext) || !ext.equals(StringUtil.MD5("CP" + keyWord + dictIds + "Dict"))) { // 非法访问
			log.debug("检索失败，验证字段[" + ext + "]有错");
			ai.getController().renderText("非法访问");
			return;
		}
		String ip = getIpAddr(ai);
		if(!isPass(ai)) {
			log.debug("IP["+ip+"]查询太频繁，拒绝访问");
			ai.getController().renderText("查询太频繁，请稍后再试！");
			return;
		}
		log.debug("检索成功，关键词：" + keyWord + "，辞书范围：" + dictIds + "，验证字段：" + ext + "，IP地址：" + ip);
		ai.invoke();
	}
	
	/**
	 * 根据请求的IP判断时间差，是否可以访问
	 * @param ai
	 * @return
	 */
	public boolean isPass(ActionInvocation ai) {
		String ip = getIpAddr(ai);
		long currentTime = new Date().getTime();
		long searchTime = getSearchTimeByIp(ip);
		if(searchTime==0) {
			SearchController.ipMap.put(getIpAddr(ai), currentTime); //存入ip+请求时间
			return true;
		} else {
			SearchController.ipMap.remove(ip);
			SearchController.ipMap.put(getIpAddr(ai), currentTime);
			if(currentTime-searchTime<1000) { //时间间隔<1s
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * 根据IP获取当问时间
	 * @param ip
	 * @return
	 */
	public long getSearchTimeByIp(String ip) {
		Map map = SearchController.ipMap;
		return StringUtil.StringToLong(StringUtil.ObjectToString(map.get(ip)));
	}
	
	/**
	 * 获取客户端IP地址
	 * @param ai
	 * @return
	 */
	private String getIpAddr(ActionInvocation ai) {
		String ipAddress = null;
		ipAddress = ai.getController().getRequest().getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = ai.getController().getRequest().getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = ai.getController().getRequest().getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = ai.getController().getRequest().getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
}
