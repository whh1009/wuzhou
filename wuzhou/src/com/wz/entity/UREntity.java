package com.wz.entity;

import java.io.Serializable;

/**
 * 用于封装显示到前段的用户和角色实体
 * @author whh
 *
 */
@SuppressWarnings("serial")
public class UREntity implements Serializable{
	private Integer user_id;
	private String user_name;
	private String nick_name;
	private String user_pwd;
	private String email;
	private Integer role_id;
	private String role_name;
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getUser_pwd() {
		return user_pwd;
	}
	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getRole_id() {
		return role_id;
	}
	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	
	
}
