package com.wz.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wz.common.ColumnMap;
import com.wz.common.ConfigInfo;
import com.wz.dao.ConfigDao;
import com.wz.entity.ConfigEntity;
import com.wz.util.StringUtil;



public class ConfigService {
	private ConfigDao configDao;
	public ConfigService() {
		configDao = new ConfigDao();
	}
	
	/**
	 * 根据用户id获取配置实体
	 * @param userId
	 * @return
	 */
	public ConfigEntity getConfigEntityByUserId(Integer userId) throws Exception {
		return configDao.getConfigByUserId(userId);
	}
	
	/**
	 * 根据用户id获取要显示的字段
	 * @param userId
	 * @return 
	 */
	public ConfigEntity getConfigByUserId(Integer userId) throws Exception{
		ConfigEntity configEntity = configDao.getConfigByUserId(userId);
		if(configEntity!=null&&configEntity.getShow_column()!=null&&!"".equals(configEntity.getShow_column())) {
			return configEntity;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据用户id获取要导出的字段
	 * @param userId
	 * @return
	 */
	public String getExportColumnByUserId(int userId) throws Exception{
		ConfigEntity configEntity = configDao.getConfigByUserId(userId);
		if(configEntity!=null&&configEntity.getExport_column()!=null&&!"".equals(configEntity.getExport_column())) {
			return configEntity.getExport_column();
		} else {
			return ConfigInfo.DEFAULT_EXPORT_COLUMN;
		}
	}
	
	/**
	 * 根据用户id获取检索字段
	 * @param userId
	 * @return
	 */
	public String getSearchColumnByUserId(int userId) throws Exception{
		ConfigEntity configEntity = configDao.getConfigByUserId(userId);
		if(configEntity!=null&&configEntity.getSearch_column()!=null&&!"".equals(configEntity.getSearch_column())) {
			return configEntity.getSearch_column();
		} else {
			return ConfigInfo.DEFAULT_SEARCH_COLUMN;
		}
	}
	
	/**
	 * 根据用户id获取要显示的字段
	 * @param userId
	 * @return 组装XML
	 */
	public String getShowColumnByUserId(int userId) throws Exception{
		ConfigEntity configEntity = configDao.getConfigByUserId(userId);
		if(configEntity!=null&&configEntity.getShow_column()!=null&&!"".equals(configEntity.getShow_column())) {
			return configEntity.getShow_column();
		} else {
			return ConfigInfo.DEFAULT_SHOW_COLUMN;
		}
	}
	
	
	public String getConfigColumnXmlByUserId(int userId) throws Exception{
		String xmlStr = "<column>";
		String showColumn = ConfigInfo.DEFAULT_SHOW_COLUMN;
		String searchColumn = ConfigInfo.DEFAULT_SHOW_COLUMN;
		ConfigEntity ce = getConfigByUserId(userId);
		if(ce!=null) {
			if(ce.getShow_column()!=null&&!"".equals(ce.getShow_column())) {
				showColumn = ce.getShow_column();
			}
			if(ce.getSearch_column()!=null&&!"".equals(ce.getSearch_column())) {
				searchColumn = ce.getSearch_column();
			}
		}
		String [] showColumnArr = showColumn.split(",");
		for(String column : showColumnArr) {
			xmlStr = xmlStr + "<item cname='"+ColumnMap.getBookTableCnByColumnName(column.trim())+"' ename='"+column+"' />";
		}
		String [] searchColumnArr = searchColumn.split(",");
		for(String column : searchColumnArr) {
			xmlStr = xmlStr + "<searchItem cname='"+ColumnMap.getBookTableCnByColumnName(column)+"' />";
		}
		xmlStr = xmlStr + "</column>";
		return xmlStr;
	}
	
	
	/**
	 * map的value转list
	 * @param map
	 * @return
	 */
	public List<String> mapValToListStr(Map<String, String> map) throws Exception{
		List<String> list = new ArrayList<String>();
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while(it.hasNext()){
			list.add(it.next().getValue());
		}
		return list;
	}
	
	/**
	 * 把逗号分隔的字符串放到list中
	 * @param str 转中文名
	 * @return
	 */
	public List<String> stringToList(String str) throws Exception{
		List<String> list = new ArrayList<String>();
		for(String s : str.split(",")) {
			list.add(ColumnMap.getBookTableCnByColumnName(s.trim())); //转中文名
		}
		return list;
	}
	
	/**
	 * 把逗号分隔的字符串放到list中
	 * @param str 不转中文名
	 * @return
	 */
	public List<String> stringToList2(String str) throws Exception{
		List<String> list = new ArrayList<String>();
		for(String s : str.split(",")) {
			list.add(s.trim()); //转中文名
		}
		return list;
	}
	
	/**
	 * map做减法，map2-map1
	 * @param map1 大map
	 * @param map2 小map
	 */
	public static Map<String, String> subtractMap(Map<String,String> map1, Map<String,String> map2)throws Exception{
		Iterator<Entry<String, String>> it1 = map1.entrySet().iterator();
		while(it1.hasNext()){
			String key1 = it1.next().getKey();
			Iterator<Entry<String, String>> it2 = map2.entrySet().iterator();
			while(it2.hasNext()){
				String key2 = it2.next().getKey();
				if(key1.equals(key2)){
					it2.remove();
					break;
				}
			}
		}
		return map2;
	}

	/**
	 * 根据sql更新配置表
	 * @param sql
	 * @return
	 */
	public int addOrUpdateConfig(String type,int userId, String value) throws Exception{
		return configDao.addOrUpdateConfig(type,userId,value);
	}

	/**
	 * 把中文列名名转为英文名
	 * @param rightShowString
	 * @param flag 1显示字段 2导出字段 3检索字段
	 * @return
	 */
	public String cnNameToEnName(String columns, int flag) throws Exception{
		String out = "";
		if(columns==null||"".equals(columns)||"null".equals(columns.toLowerCase())) {
			if(flag==1) {
				return ConfigInfo.DEFAULT_SHOW_COLUMN;
			} else if(flag==2){
				return ConfigInfo.DEFAULT_EXPORT_COLUMN;
			} else {
				return ConfigInfo.DEFAULT_SEARCH_COLUMN;
			}
		} else {
			for(String str : columns.split(",")) {
				out = out + ColumnMap.getBookTableColumnNameByCn(str.trim())+",";
			}
		}
		return StringUtil.ignoreComma(out);
	}
}
