package com.wz.service;

import java.util.List;

import com.wz.dao.LogDao;
import com.wz.entity.LogEntity;

public class LogService {
	private LogDao logDao;
	public LogService() {
		logDao = new LogDao();
	}
	
	public List<LogEntity> getLogList() throws Exception {
		return logDao.getLogList();
	}
	
	public List<LogEntity>  getLogListByCondition(String name,String content) throws Exception{
		return logDao.getLogListByCondition(name,content);
	}
	
	/**
	 * 添加日志文件
	 * @param logEntity
	 * @return
	 */
	public int addLog(LogEntity logEntity) throws Exception{
		return logDao.addLog(logEntity);
	}
	
	/**
	 * 删除日志文件
	 * @param modify_id
	 * @return
	 */
	public int deleteLog(Integer modify_id) throws Exception{
		return logDao.deleteLog(modify_id);
	}
}
