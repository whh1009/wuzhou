package com.cp.model;

import java.util.List;

import com.cp.common.Constrant;
import com.cp.util.StringUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class User extends Model<User> {
	public static User userDao= new User();
	
	/**
	 * 判断用户名是否重复
	 * @param userName
	 * @return
	 */
	public boolean checkUserName(String userName) {
		String sql = "select * from cp_user where user_name ='"+userName+"'";
		List<User> tagUser =userDao.find(sql);
		if(tagUser!=null&&!tagUser.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 登录
	 */
	public User login(String userName, String pwd) {
		String sql="select * from cp_user where user_name = '"+userName+"' and user_pwd = '"+StringUtil.MD5(pwd)+"'";
		List<User> tagUser =userDao.find(sql);
		if(tagUser!=null&&!tagUser.isEmpty()){
			return tagUser.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 获取用户列表
	 * @param userPage
	 * @return
	 * @author sunlei
	 */
	public Page<Record> getUserList(int userPage){
		return Db.paginate(userPage, Constrant.PAGE_SIZE, "select * ", " from cp_user");
	}
	
}
