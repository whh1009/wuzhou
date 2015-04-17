package com.wz.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.wz.common.ColumnMap;
import com.wz.common.ConfigInfo;
import com.wz.common.PageEntity;
import com.wz.entity.RoleEntity;
import com.wz.entity.RoleMenuEntity;
import com.wz.entity.UREntity;
import com.wz.entity.UserEntity;
import com.wz.entity.UserRoleEntity;
import com.wz.service.UserService;
import com.wz.util.StringUtil;

public class UserAction extends ActionSupport{
	private UserService userService;
	private List<UserEntity> userList;
	private List<RoleEntity> roleList;
	private List<UREntity> urList;
	private UserEntity userEntity;
	private RoleEntity roleEntity;
	private UserRoleEntity userRoleEntity;
	private PageEntity pageEntity;
	private String menuXml; //给角色分配菜单时，把菜单组装XML
	
	public PageEntity getPageEntity() {
		return pageEntity;
	}
	public void setPageEntity(PageEntity pageEntity) {
		this.pageEntity = pageEntity;
	}
	public void setUserList(List<UserEntity> userList) {
		this.userList = userList;
	}
	public List<RoleEntity> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<RoleEntity> roleList) {
		this.roleList = roleList;
	}
	public UserEntity getUserEntity() {
		return userEntity;
	}
	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	public RoleEntity getRoleEntity() {
		return roleEntity;
	}
	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
	}
	public UserRoleEntity getUserRoleEntity() {
		return userRoleEntity;
	}
	public void setUserRoleEntity(UserRoleEntity userRoleEntity) {
		this.userRoleEntity = userRoleEntity;
	}
	public List<UREntity> getUrList() {
		return urList;
	}
	public void setUrList(List<UREntity> urList) {
		this.urList = urList;
	}
	public String getMenuXml() {
		return menuXml;
	}
	public void setMenuXml(String menuXml) {
		this.menuXml = menuXml;
	}
	public UserAction() {
		userService = new UserService();
	}

	/**
	 * 用户信息列表
	 * @return
	 * @throws Exception
	 */
	public String userList() throws Exception {
		List<UserEntity> list = userService.showUser();
		//userList = userService.showUserByPage(1, ConfigInfo.PAGE_ROW_COUNT);
		roleList = userService.getRoleList();
		urList = userService.showUrByPage(1, ConfigInfo.PAGE_ROW_COUNT);
		pageEntity = new PageEntity();
		pageEntity.setCurrentPage(1);
		pageEntity.setPageCount(pageEntity.getPageCount(ConfigInfo.PAGE_ROW_COUNT, list==null?0:list.size()));
		pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
		pageEntity.setRowCount(list==null?0:list.size());
		return Action.SUCCESS;
	}
	
	/**
	 * 用户信息分页
	 * @throws Exception
	 */
	public void getUserListByPage() throws Exception {
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			int showPage = StringUtil.StringToInt(request.getParameter("currentPage"));
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
	//		List<UserEntity> userList = userService.showUserByPage(showPage, ConfigInfo.PAGE_ROW_COUNT);
			List<UREntity> urList = userService.showUrByPage(showPage, ConfigInfo.PAGE_ROW_COUNT);
			pageEntity = new PageEntity();
			pageEntity.setCurrentPage(showPage);
			Map map = new HashMap();
			map.put("urList", urList);
			map.put("pageEntity", pageEntity);
			JSONObject json = JSONObject.fromObject(map);
			out.print(json.toString());
			out.flush();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public void getUserInfo() throws Exception {
		Map map = new HashMap();
		List<UserEntity> userList = userService.showUser();
		PageEntity pageEntity = new PageEntity();
		pageEntity.setCurrentPage(1);
		pageEntity.setPageCount(pageEntity.getPageCount(ConfigInfo.PAGE_ROW_COUNT, userList==null?0:userList.size()));
		pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
		pageEntity.setRowCount(userList==null?0:userList.size());
		JSONObject json = JSONObject.fromObject(map);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
//		System.out.println("userjson："+json.toString());
		out.flush();
		out.close();
	}
	
	/**
	 * 返回创建用户界面
	 * @return
	 */
	public  String addUserPage() throws Exception{
		//初始化角色信息
		roleList = userService.getRoleList();
		return Action.SUCCESS;
	}
	
	
	/**
	 * 创建用户
	 * @return
	 * @throws Exception
	 */
	public String addUser() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		userEntity.setLogin_times(0);
		userEntity.setReg_time(new Date());
		userEntity.setStatus(1);	
		UserEntity ue = userService.addUser(userEntity);
		userRoleEntity.setUserEntity(ue);
		userService.addUserRole(userRoleEntity);
		return Action.SUCCESS;
	}
	
	/**
	 * session中存在用户信息则返回首页，不存在则重新登录
	 * @return
	 */
	public String toLogin() {
		//有些是授权页面不能直接定向
		UserEntity userSession = (UserEntity)ActionContext.getContext().getSession().get("userEntity");
		return Action.SUCCESS;
	}
	
	/**
	 * 登录
	 * @throws Exception
	 */
	public void login() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		String userName = request.getParameter("userName");
		String pwd = request.getParameter("password");
		System.out.println("["+userName+"]登录");
		UserEntity userEntity = userService.checkLogin(userName, pwd);
		System.out.println(new Date().getTime());
		if(userEntity==null) {
			out.write("-1");//登录失败
		} else {//登录成功

			userEntity.setLogin_times(userEntity.getLogin_times()+1);
			userEntity.setLast_login_time(new Date()); //需要更新mysql包，版本不同会报错
			userService.updateUser(userEntity);
			//获取菜单
			UserRoleEntity userRoleEntity = userService.getUserRoleByUserEntity(userEntity);
			if(userRoleEntity==null) {//用户角色获取失败
				out.write("0");
				return;
			}
			RoleEntity roleEntity = userRoleEntity.getRoleEntity();
			List<RoleMenuEntity> roleMenuEntityList = userService.getMenuListByRoleEntity(roleEntity);
			String navigation = userService.getMenuNavByRoleMenuList(roleMenuEntityList, userEntity);
			//当前登录的用户放入session
			ActionContext.getContext().getSession().put("userEntity", userEntity);
			//角色存入session
			ActionContext.getContext().getSession().put("roleEntity", roleEntity);
			//角色表单控制xml存入session
			ActionContext.getContext().getSession().put("roleFormConfig", ConfigInfo.ROLE_FROM_CONFIG);
			//角色菜单存入session
			ActionContext.getContext().getSession().put("roleMenuXml", userService.getXmlByRoleMenuList(roleMenuEntityList));
			//菜单存入session
			ActionContext.getContext().getSession().put("navigation", navigation);
			out.write("1");
		}
		out.close();
	}
	
	/**
	 * 判断用户名是否已经被注册
	 */
	public void checkUserName() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String userName = request.getParameter("userName");
		int used = userService.checkUserName(userName);
		out.write(used+"");
		out.close();
	}
	
	/**
	 * 删除用户
	 * @throws Exception
	 */
	public void deleteUser() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		int userId = StringUtil.StringToInt(request.getParameter("userId"));
		
		UserEntity ue = userService.getUserEntityByUserId(userId);
		//删除用户对应的角色关系
		int flag = userService.deleteUser(userId);
		UserRoleEntity ure = userService.getUserRoleByUserEntity(ue);
		userService.deleteUserRoleEntity(ure.getUser_role_id());
		out.write(flag+"");
		out.close();
	}
	
	/**
	 * 修改密码
	 * @throws Exception
	 */
	public void modifyPwd() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		String userPwd = StringUtil.ObjectToString(request.getParameter("userPwd"));
		PrintWriter out = null;
		out = response.getWriter();
		if("".equals(userPwd)) { //为获取到新密码
			out.write("0");
		}
		Object obj = request.getSession().getAttribute("userEntity");
		if(obj==null) {//session为空
			out.write("-1");
		} else {
			UserEntity ue = (UserEntity) obj;
			ue.setUser_pwd(userPwd);
			int flag = userService.updateUser(ue);
			if(flag==1) {
//				System.out.println("用户【"+ue.getNick_name()+"】更新了密码");
				out.write("1");
			} else {
//				System.out.println("用户【"+ue.getNick_name()+"】更新密码失败了");
				out.write("-1");
			}
		}
		out.close();
	}
	
	/**
	 * 更新用户信息
	 * @return
	 * @throws Exception
	 */
	public void updateUser() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		int userId = StringUtil.StringToInt(request.getParameter("userId"));
		String userName = StringUtil.ObjectToString(request.getParameter("userName"));
		String nickName = StringUtil.ObjectToString(request.getParameter("nickName"));
		String userPwd = StringUtil.ObjectToString(request.getParameter("userPwd"));
		String email = StringUtil.ObjectToString(request.getParameter("email"));
		int roleId = StringUtil.StringToInt(request.getParameter("roleId"));
		UserEntity ue = userService.getUserEntityByUserId(userId);
		
		if(ue==null) {
			out.write("0");
			return;
		}
		ue.setUser_name(userName);
		ue.setNick_name(nickName);
		ue.setUser_pwd(userPwd);
		ue.setUser_pwd(userPwd);
		ue.setEmail(email);
		int flag = userService.updateUser(ue);
//		System.out.println("更新用户表："+flag);
		if(flag==0) {
			out.write("0");
			return;
		}
		if(roleId!=-1) { //修改用户角色
			RoleEntity re = userService.getRoleEntityByRoleId(roleId);
			if(re==null) {
				out.write("0");
				return;
			}
			UserRoleEntity ure = userService.getUserRoleByUserEntity(ue);
			int fout = 0;
			if(ure==null) { //创建用户角色
				ure = new UserRoleEntity();
				ure.setUserEntity(ue);
				ure.setRoleEntity(re);
				fout = userService.addUserRoleEntity(ure);
			} else { //修改
				ure.setRoleEntity(re);
				fout = userService.updateUserRoleEntity(ure);
			}
			out.write(fout+"");
			return;
		}
		out.write("1");
		out.close();
	}
	
	/**
	 * 添加角色
	 * @return
	 */
	public String addRolePage() throws Exception{
		roleList = userService.getRoleList();
		menuXml = userService.getMenuXml();
		return Action.SUCCESS;
	}
	
	public void checkRoleName() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String roleName = StringUtil.ObjectToString(request.getParameter("roleName"));
		if("".equals(roleName)) { //未获取到角色名
			out.write("-1");
			return;
		} else {
			boolean flag = userService.checkRoleName(roleName);
			if(flag) {
				out.write("1"); //已存在
			} else {
				out.write("0"); //不存在
			}
		}
		out.close();
	}
	
	/**
	 * 添加角色
	 * @return
	 */
	public String addRole() throws Exception{
		roleEntity.setStatus(1);
		int flag = userService.addRole(roleEntity);
		if(flag == 1) {
			return Action.SUCCESS;
		} else {
			return Action.INPUT;
		}
		
	}
	
	/**
	 * 保存权限分配
	 * @return
	 */
	public void saveRoleMenu() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		int roleId = StringUtil.StringToInt(request.getParameter("roleId"));
		String menuId = StringUtil.ObjectToString(request.getParameter("menuId"));
		if(roleId==0||"".equals(menuId)) { //未获取到参数
			out.write("-1");
			return;
		}
		menuId = StringUtil.ignoreComma(menuId);
		//风险很大，应先根据roleId获取menuId，如果删除后，插入失败，则就会失去权限。
		boolean delFlag = userService.removeRoleMenuByRoleId(roleId);
		boolean addFlag = userService.addRoleMenu(roleId, menuId);
		if(delFlag&&addFlag) {
			out.write("1"); //修改成功
		} else {
			out.write("0"); //修改失败
		}
		out.close();
	}
	
}
