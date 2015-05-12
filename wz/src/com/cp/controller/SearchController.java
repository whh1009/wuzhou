package com.cp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cp.interceptor.LoginInterceptor;
import com.cp.interceptor.SearchInterceptor;
import com.cp.model.Cpdict;
import com.cp.model.Dict;
import com.cp.util.StringUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;

public class SearchController extends Controller {
	private static Logger log = Logger.getLogger("presentOperatorLog");
	public static Map<String, Long> ipMap = new HashMap<String, Long>();
	
	/**
	 * 首页
	 */
	@Before(LoginInterceptor.class)
	public void index() {
		render("Search.jsp");
	}

	/**
	 * 初始化词典名
	 */
	public void initDictName() {
		renderJson(JsonKit.toJson(Dict.dictDao.getDict()));
	}

	/**
	 * 和汇智对接，被动检索
	 * 要求: （拦截器拦截）
	 * 	参数1 keyWord 检索关键词 模糊检索（任意字符“+“，一个字符“#”）（我+，检索以我开头的任意字符，我#，检索我后的一个字符）
	 * 	参数2 dictIds 辞书范围 1-29，中间以逗号(",")分割，如：1,2,3
	 * 	参数3 ext "CP参数1参数2Dict"的MD5加密
	 */
	@Before(SearchInterceptor.class)
	public void cpSearch() {
		String keyWord = StringUtil.ObjectToString(getPara("keyWord"));
		String dictIds = StringUtil.ObjectToString(getPara("dictIds"));
		int searchCol = getParaToInt("searchCol") == null ? 0 : getParaToInt("searchCol");
		dictIds = StringUtil.ignoreComma(dictIds);
		List<Cpdict> list = Cpdict.dictDao.search(dictIds, keyWord, searchCol);
		setAttr("json", JsonKit.toJson(list));
		setAttr("dictIds", dictIds);
		render("CPSearch.jsp");
	}

	/**
	 * 检索
	 */
	public void searchWord() {
		String keyWord = StringUtil.ObjectToString(getPara("keyWord"));
		if ("".equals(keyWord)) { // 关键词是空
			renderJson(JsonKit.toJson(0));
			return;
		}
		String dictIds = StringUtil.ObjectToString(getPara("dictIds"));
		if ("".equals(dictIds)) { // 辞书是空
			renderJson(JsonKit.toJson("1"));
			return;
		}
		log.debug("检索关键词：" + keyWord + "，辞书范围：" + dictIds);
		int searchCol = getParaToInt("searchCol") == null ? 0 : getParaToInt("searchCol");
		dictIds = StringUtil.ignoreComma(dictIds);
		List<Cpdict> list = Cpdict.dictDao.search(dictIds, keyWord, searchCol);
		renderJson(JsonKit.toJson(list));
	}
}
