package com.wz.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class UserEntity implements Serializable{
	private Integer user_id;
	private String user_name;
	private String nick_name;
	private String user_pwd;
	private Date reg_time;
	private Date last_login_time;
	private Integer login_times;
	public Date getReg_time() {
		return reg_time;
	}
	public void setReg_time(Date regTime) {
		reg_time = regTime;
	}
	public Date getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(Date lastLoginTime) {
		last_login_time = lastLoginTime;
	}
	public Integer getLogin_times() {
		return login_times;
	}
	public void setLogin_times(Integer loginTimes) {
		login_times = loginTimes;
	}
	private String email;
	private Integer status;
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer userId) {
		user_id = userId;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nickName) {
		nick_name = nickName;
	}
	public String getUser_pwd() {
		return user_pwd;
	}
	public void setUser_pwd(String userPwd) {
		user_pwd = userPwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
