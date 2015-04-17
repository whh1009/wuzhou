package com.wz.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserRoleEntity implements Serializable{
	private Integer user_role_id;
	private UserEntity userEntity;
	private RoleEntity roleEntity;
	public Integer getUser_role_id() {
		return user_role_id;
	}
	public void setUser_role_id(Integer user_role_id) {
		this.user_role_id = user_role_id;
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
	
}
