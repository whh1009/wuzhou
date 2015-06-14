package com.wz.service;

import java.util.Collections;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.wz.dao.UserDao;
import com.wz.entity.MenuEntity;
import com.wz.entity.RoleEntity;
import com.wz.entity.RoleMenuEntity;
import com.wz.entity.UREntity;
import com.wz.entity.UserEntity;
import com.wz.entity.UserRoleEntity;
import com.wz.util.MenuSort;

public class UserService {
	private UserDao userDao;
	public UserService() {
		userDao = new UserDao();
	}
	
	/**
	 * 添加用户
	 * @param userEntity
	 */
	public UserEntity addUser(UserEntity userEntity) throws Exception {
		return this.userDao.addUser(userEntity);
	}

	/**
	 * 添加角色
	 * @param roleEntity
	 * @return
	 */
	public int addRole(RoleEntity roleEntity) throws Exception {
		return userDao.addRole(roleEntity);
	}
	
	/**
	 * 添加用户角色
	 * @param userRoleEntity
	 */
	public void addUserRole(UserRoleEntity userRoleEntity) throws Exception {
		this.userDao.addUserRole(userRoleEntity);
	}
	
	/**
	 * 删除用户
	 * @param id
	 */
	public int deleteUser(Integer id) throws Exception {
		return this.userDao.deleteUser(id);
	}
	
	/**
	 * 删除用户对应的角色关系
	 * @param ure
	 * @return
	 */
	public int deleteUserRoleEntity(Integer urId) throws Exception {
		return userDao.deleteUserRoleEntity(urId);
	}
	
	/**
	 * 显示所有用户
	 * @return
	 */
	public List<UserEntity> showUser() throws Exception {
		return this.userDao.list();
	}
	
	/**
	 * 分页查询用户信息
	 * @param currentPage 当前页
	 * @param pageRowCount 每页显示记录数
	 * @param hql
	 * @return
	 */
	public List<UserEntity> showUserByPage(int currentPage, int pageRowCount)throws Exception  {
		return userDao.showUserByPage(currentPage, pageRowCount);
	}
	
	/**
	 * 
	 * @param currentPage
	 * @param pageRowCount
	 * @return
	 */
	public List<UREntity> showUrByPage(int currentPage, int pageRowCount) throws Exception {
		return userDao.showUrByPage(currentPage, pageRowCount);
	}

	
	/**
	 * 登录验证
	 * @param userName 用户名
	 * @param pwd 密码
	 * @return
	 */
	public UserEntity checkLogin(String userName, String pwd) throws Exception {
		return userDao.checkLogin(userName, pwd);
	}
	
	
	/**
	 * 检查用户名是否已经被使用
	 * @param userName
	 * @return 被使用返回1，没有被使用返回0，查询出错返回-1
	 */
	public int checkUserName(String userName) throws Exception {
		return userDao.checkUserName(userName);
	}
	
	
	/**
	 * 更新用户
	 * @param userEntity
	 * @return
	 */
	public int updateUser(UserEntity userEntity) throws Exception {
		return userDao.updateUser(userEntity);
	}
	
	/**
	 * 新增用户角色
	 * @param ure
	 * @return
	 */
	public int addUserRoleEntity(UserRoleEntity ure) throws Exception {
		return userDao.addUserRole(ure);
	}
	
	/**
	 * 更新用户角色表
	 * @param ure
	 */
	public int updateUserRoleEntity(UserRoleEntity ure) throws Exception {
		return userDao.updateUserRoleEntity(ure);
		
	}
	
	/**
	 * 根据用户id获取用户信息
	 * @param userId
	 * @return
	 */
	public UserEntity getUserEntityByUserId(int userId) throws Exception {
		return userDao.getUserEntityByUserId(userId);
	}
	
	/**
	 * 根据角色ID获取角色信息
	 * @param roleId
	 * @return
	 */
	public RoleEntity getRoleEntityByRoleId(int roleId) throws Exception {
		return userDao.getRoleEntityByRoleId(roleId);
	}
	
	/**
	 * 获取用户角色
	 * @return
	 */
	public List<RoleEntity> getRoleList() throws Exception {
		return userDao.getRoleList();
	}
	
	/**
	 * 根据用户信息获取用户角色信息
	 * @param userEntity
	 * @return
	 */
	public UserRoleEntity getUserRoleByUserEntity(UserEntity userEntity) throws Exception {
		return userDao.getUserRoleByUser(userEntity);
	}

	/**
	 * 根据角色获取菜单list
	 * @param roleEntity
	 * @return
	 */
	public List<RoleMenuEntity> getMenuListByRoleEntity(RoleEntity roleEntity) throws Exception {
		return userDao.getMenuListByRoleEntity(roleEntity);
	}
	
	public String getMenuNavByRoleMenuList(List<RoleMenuEntity> roleMenuEntityList, UserEntity userEntity) throws Exception {
		String xml = getXmlByRoleMenuList(roleMenuEntityList);
		if("".equals(xml)) {
			return "";
		}
		return getMenuNavByMenuXml(xml, userEntity);
	}

	/**
	 * 根据menu 生成xml
	 * @param roleMenuEntityList
	 * @return
	 */
	public String getXmlByRoleMenuList(List<RoleMenuEntity> roleMenuEntityList) throws Exception {
		if(roleMenuEntityList==null||roleMenuEntityList.size()==0) return "";
		StringBuffer xmlStr = new StringBuffer("<menu>");
		for(RoleMenuEntity rme : roleMenuEntityList) {
			MenuEntity menuEntity = rme.getMenuEntity();
			xmlStr.append("<item menuId='").append(menuEntity.getMenu_id())
			.append("' menuName='").append(menuEntity.getMenu_name())
			.append("' menuAction='").append(menuEntity.getMenu_action())
			.append("' menuLeval='").append(menuEntity.getMenu_leval())
			.append("' menuPid='").append(menuEntity.getMenu_pid())
			.append("' menuDesc='").append(menuEntity.getMenu_desc())
			.append("' menuFlag='").append(menuEntity.getMenu_flag())
			.append("' />");
		}
		xmlStr.append("</menu>");
		return xmlStr.toString();
	}
	
	/**
	 * 根据menu xml生成menu导航
	 * @param xmlStr
	 * @return
	 */
	private String getMenuNavByMenuXml(String xmlStr, UserEntity userEntity) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<nav class='navbar navbar-default' role='navigation'><div class='navbar-header' style='margin-right:90px'><button type='button' class='navbar-toggle' data-toggle='collapse' data-target='#bs-example-navbar-collapse-1'><span class='sr-only'>菜单导航</span><span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span></button><span class='navbar-brand' href='javascript:;'><img src='../images/logo.png' style='float:left;'/></span></div><div class='collapse navbar-collapse' id='bs-example-navbar-collapse-1' style='margin-top:5px'><ul class='nav navbar-nav'>");
		try {
			Document doc = DocumentHelper.parseText(xmlStr);
			parserItem(doc, "0", sb);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		sb.append("</ul>");
		sb.append("<ul class='nav navbar-nav navbar-right'> <li> <a href='javascript:;' class='dropdown-toggle' data-toggle='dropdown'><strong> "+userEntity.getNick_name()+" </strong> <span class='caret'></span>&nbsp;&nbsp;&nbsp;&nbsp;</a> <ul class='dropdown-menu'><li><a href='javascript:modifyPwd();'> 密码修改 </a></li><li><a href='javascript:logout();'>退出</a></li></ul>  </li> </ul>");
		sb.append("</div></nav>");
		return sb.toString();
	}
	
	/**
	 * 解析item，生成菜单
	 * @param doc
	 * @param mId
	 * @param sb
	 */
	private void parserItem (Document doc, String mId, StringBuffer sb) throws Exception {
		List<Element> itemList = doc.selectNodes("//menu/item[@menuPid='"+mId+"']");
		for(Element itemEle : itemList) {
			String menuId = itemEle.attributeValue("menuId");
			String menuAction = itemEle.attributeValue("menuAction");
			String menuName = itemEle.attributeValue("menuName");
			String menuFlag = itemEle.attributeValue("menuFlag");
			List<Element> subItemList = doc.selectNodes("//menu/item[@menuPid='"+menuId+"']");
			if(subItemList==null||subItemList.size()==0) {
				sb.append("<li><a href='").append(menuAction).append("'>").append("<span class='"+menuFlag+"' />&nbsp;&nbsp;&nbsp;").append(menuName).append(" </a></li>");
			} else {
				sb.append("<li class='dropdown'><a href='javascript:;' class='dropdown-toggle' data-toggle='dropdown'>")
				.append(menuName).append(" <b class='caret'></b> </a><ul class='dropdown-menu'>");
				parserItem(doc, menuId, sb);
				sb.append("</ul></li>");
			}
		}
	}

	/**
	 * 判断角色名是否重复
	 * @param roleName
	 * @return 存在返回true， 不存在返回false；
	 */
	public boolean checkRoleName(String roleName) throws Exception {
		String hql = "from RoleEntity where role_name = '"+roleName+"'";
		return userDao.checkRoleName(hql);
	}

	/**
	 * 组装菜单XML
	 * @return
	 */
	public String getMenuXml() throws Exception {
		List<MenuEntity> menuList = userDao.getMenuEntity();
		if(menuList==null) return "";
		String menuXml = "<menu>";
		for(MenuEntity menuEntity : menuList) { //拼接menu
			menuXml = menuXml + "<item menuId='"+menuEntity.getMenu_id()+"' menuName='"+menuEntity.getMenu_name()+"' menuLeval='"+menuEntity.getMenu_leval()+"' menuPid='"+menuEntity.getMenu_pid()+"' />";
		}	
		List<RoleMenuEntity> roleMenuList = userDao.getMenuListByRoleEntity(null);
		if(roleMenuList!=null) {
			for(RoleMenuEntity rme : roleMenuList) {
				menuXml = menuXml + "<roleItem roleId='"+rme.getRoleEntity().getRole_id()+"' menuId='"+rme.getMenuEntity().getMenu_id()+"' />";
			}
		}
		menuXml = menuXml + "</menu>";
		return menuXml;
	}

	/**
	 * 根据roleId删除所有权限
	 * @param roleId
	 */
	public boolean removeRoleMenuByRoleId(int roleId) throws Exception {
		return userDao.removeRoleMenuById(roleId);
	}

	/**
	 * 根据roleId添加菜单
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public boolean addRoleMenu(int roleId, String menuId) throws Exception {
		return userDao.addRoleMenu(roleId, menuId);
	}

	
	public List<UserEntity> userList() throws Exception {
		return userDao.list();
	}
	
	public List<UserEntity> getUserList() throws Exception {
		return userDao.getUserList();
	}

	public List<UserEntity> getUserListByHql(String hql) throws Exception {
		return userDao.getUserListByHql(hql);
	}
	
}
