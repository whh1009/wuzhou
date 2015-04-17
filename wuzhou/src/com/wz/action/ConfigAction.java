package com.wz.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.ListUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.wz.common.ColumnMap;
import com.wz.entity.ConfigEntity;
import com.wz.entity.UserEntity;
import com.wz.service.ConfigService;
/**
 * 数目信息显示和导出字段配置
 * @author Administrator
 *
 */
public class ConfigAction extends ActionSupport {
	private ConfigService configService;
	private List<String> leftShowList = new ArrayList<String>(); //保存没有显示的字段
	private List<String> rightShowList = new ArrayList<String>();; //保存该用户已经有的字段
	private String rightShowString; //用于获取配置的字段值
	private List<String> leftExportList = new ArrayList<String>();; //保存没有导出字段
	private List<String> rightExportList = new ArrayList<String>();; //保存已经导出的字段
	private String rightExportString; //用于获取导出配置的字段值
	private List<String> leftSearchList = new ArrayList<String>(); //保存没有用于检索的字段
	private List<String> rightSearchList = new ArrayList<String> (); //保存已经用于检索的字段
	private String rightSearchString;//用于获取检索配置的字段值
	
	//private String showOrExport; //用于标记，判断提交过来的是显示还是导出字段
	public List<String> getLeftShowList() {
		return leftShowList;
	}

	public void setLeftShowList(List<String> leftShowList) {
		this.leftShowList = leftShowList;
	}

	public List<String> getRightShowList() {
		return rightShowList;
	}

	public void setRightShowList(List<String> rightShowList) {
		this.rightShowList = rightShowList;
	}

	public List<String> getLeftExportList() {
		return leftExportList;
	}

	public void setLeftExportList(List<String> leftExportList) {
		this.leftExportList = leftExportList;
	}

	public List<String> getRightExportList() {
		return rightExportList;
	}

	public void setRightExportList(List<String> rightExportList) {
		this.rightExportList = rightExportList;
	}
	public String getRightShowString() {
		return rightShowString;
	}

	public void setRightShowString(String rightShowString) {
		this.rightShowString = rightShowString;
	}

	public String getRightExportString() {
		return rightExportString;
	}

	public void setRightExportString(String rightExportString) {
		this.rightExportString = rightExportString;
	}

	public List<String> getLeftSearchList() {
		return leftSearchList;
	}

	public void setLeftSearchList(List<String> leftSearchList) {
		this.leftSearchList = leftSearchList;
	}

	public List<String> getRightSearchList() {
		return rightSearchList;
	}

	public void setRightSearchList(List<String> rightSearchList) {
		this.rightSearchList = rightSearchList;
	}

	public String getRightSearchString() {
		return rightSearchString;
	}

	public void setRightSearchString(String rightSearchString) {
		this.rightSearchString = rightSearchString;
	}

	public ConfigAction() {
		configService = new ConfigService();
	}
	
	/**
	 * 配置显示字段
	 * @return
	 * @throws Exception 
	 */
	public String showColumnConfig() throws Exception {
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity user = (UserEntity)request.getSession().getAttribute("userEntity");
			if(user==null) {
				return "noLogin";
			} else {
				String showCol = configService.getShowColumnByUserId(user.getUser_id());
				if("".equals(showCol)) {
					return "noLogin";
				} else {
					rightShowList = configService.stringToList(showCol);
					List<String> colAll = configService.mapValToListStr(ColumnMap.bookTableColumnMap());
					leftShowList = ListUtils.subtract(colAll, rightShowList); //list做减法得到剩余没有显示的字段名
					return Action.SUCCESS;
				}
			}
	}
	
	/**
	 * 配置导出字段
	 * @return
	 * @throws Exception 
	 */
	public String exportColumnConfig() throws Exception {
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity user = (UserEntity)request.getSession().getAttribute("userEntity");
			if(user==null) {
				return "noLogin";
			} else {
				String exportCol = configService.getExportColumnByUserId(user.getUser_id());
				if("".equals(exportCol)) {
					return "noLogin";
				} else {
					rightExportList = configService.stringToList(exportCol);
					List<String> colAll = configService.mapValToListStr(ColumnMap.bookTableColumnMap());
					leftExportList = ListUtils.subtract(colAll, rightExportList); //list做减法得到剩余没有要到处的字段
					return Action.SUCCESS;
				}
			}	
	}
	
	/**
	 * 配置检索条件字段
	 * @return
	 * @throws Exception 
	 */
	public String searchColumnConfig() throws Exception{
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity user = (UserEntity)request.getSession().getAttribute("userEntity");
			if(user==null) {
				return "noLogin";
			} else {
				String searchCol = configService.getSearchColumnByUserId(user.getUser_id());
				if("".equals(searchCol)) {
					return "noLogin";
				} else {
					rightSearchList = configService.stringToList(searchCol);
					List<String> colAll = configService.mapValToListStr(ColumnMap.bookTableColumnMap());
					leftSearchList = ListUtils.subtract(colAll, rightSearchList); //list做减法得到剩余没有要到处的字段
					return Action.SUCCESS;
				}
			}
	}
	
	
	/**
	 * 更新显示字段
	 * @return
	 * @throws Exception 
	 */
	public String updateShowColumnConfig() throws Exception{
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity user = (UserEntity)request.getSession().getAttribute("userEntity");
			if(user==null) {
				return "noLogin";
			} else {
				rightShowString = configService.cnNameToEnName(rightShowString, 1);
				configService.addOrUpdateConfig("show",user.getUser_id(),rightShowString);
				return Action.SUCCESS;
			}
	}
	
	/**
	 * 更新导出字段
	 * @return
	 * @throws Exception 
	 */
	public String updateExportColumnConfig() throws Exception{
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity user = (UserEntity)request.getSession().getAttribute("userEntity");
			if(user==null) {
				return "noLogin";
			} else {
				rightExportString = configService.cnNameToEnName(rightExportString, 2);
				configService.addOrUpdateConfig("export",user.getUser_id(),rightExportString);
				return Action.SUCCESS;
			}
	}
	
	/**
	 * 更新导出字段
	 * @return
	 * @throws Exception 
	 */
	public String updateSearchColumnConfig() throws Exception{
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity user = (UserEntity)request.getSession().getAttribute("userEntity");
			if(user==null) {
				return "noLogin";
			} else {
				rightSearchString = configService.cnNameToEnName(rightSearchString, 3);
				configService.addOrUpdateConfig("search",user.getUser_id(),rightSearchString);
				return Action.SUCCESS;
			}
	}
}
