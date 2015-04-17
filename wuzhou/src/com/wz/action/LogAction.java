package com.wz.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.wz.common.ColumnMap;
import com.wz.common.ConfigInfo;
import com.wz.common.PageEntity;
import com.wz.entity.LogEntity;
import com.wz.service.LogService;
import com.wz.util.StringUtil;

public class LogAction extends ActionSupport {
	private LogService logService;
	public LogAction() {
		logService = new LogService();
	}
	public String logFlag;
	
	
	public String getLogFlag() {
		return logFlag;
	}

	public void setLogFlag(String logFlag) {
		this.logFlag = logFlag;
	}

	/**
	 * 返回日志管理页面
	 * @return
	 * @throws Exception
	 */
	public String logList() throws Exception {
		logFlag = "OK";
		return Action.SUCCESS;
	}
	
	/**
	 * 获取日志文件
	 * @throws Exception
	 */
	public void getLogList(){
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try{
			out = response.getWriter();
			List<LogEntity> logList = logService.getLogList();
			if(logList==null||logList.size()==0) {
				out.print("NO RECORDS...");
			} else {
				int rowCount = logList.size();
				PageEntity pageEntity = new PageEntity();
				pageEntity.setCurrentPage(1);
				pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
				pageEntity.setRowCount(rowCount);
				pageEntity.setPageCount(pageEntity.getPageCount(ConfigInfo.PAGE_ROW_COUNT, rowCount));
				map.put("logList", logList);
				map.put("pageEntity", pageEntity);
				JSONObject json = JSONObject.fromObject(map);//将map对象转换成json类型数据
				out.print(json.toString());
			}
			out.flush();
		}catch(Exception e){
			out.write("ERROR,CAN NOT GET ANY LOG,PLEASE CONTACT SYSTEM MANAGER...");
			out.flush();
		}finally{
			if(out!=null)out.close();
		}
	}
	
	/**
	 * 根据条件检索
	 * @throws Exception
	 */
	public void getlogListByParm() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String searchType = request.getParameter("searchType");
		String searchContent = request.getParameter("searchContent");
		String columnName = ColumnMap.getLogTableColumnNameByCn(searchType);
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try{
			out = response.getWriter();
			List<LogEntity> logList = logService.getLogListByCondition(columnName,searchContent);
			if(logList==null||logList.size()==0) {
				out.print("NO RECORDS...");
			} else {
				int rowCount = logList.size();
				PageEntity pageEntity = new PageEntity();
				pageEntity.setCurrentPage(1);
				pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
				pageEntity.setRowCount(rowCount);
				pageEntity.setPageCount(pageEntity.getPageCount(ConfigInfo.PAGE_ROW_COUNT, rowCount));
				map.put("logList", logList);
				map.put("pageEntity", pageEntity);
				JSONObject json = JSONObject.fromObject(map);//将map对象转换成json类型数据
				out.print(json.toString());
			}
			out.flush();
		}catch(Exception e){
			out.write("ERROR,CAN NOT GET ANY LOG,PLEASE CONTACT SYSTEM MANAGER...");
			out.flush();
		}finally{
			if(out!=null)out.close();
		}
	}
	
	/**
	 * 删除
	 * @throws Exception
	 */
	public void deleteLog() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try{
			out = response.getWriter();
			int modifyId = StringUtil.StringToInt(request.getParameter("modifyId"));
			if(modifyId!=0) { 
				int flag = logService.deleteLog(modifyId);
				if(flag==1) { //删除成功
					out.print("1");
				} else { //删除失败
					out.print("2");
				}
			}else{
				out.print("0");
			}
			out.flush();
		}catch(Exception e){
			out.print("2");
			out.flush();
		}finally{
			if(out!=null)out.close();
		}
	}
}
