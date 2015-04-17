package com.wz.entity;

import java.io.Serializable;

public class RoleEntity  implements Serializable{
	private Integer role_id;
	private String role_name;
	private Integer status;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
