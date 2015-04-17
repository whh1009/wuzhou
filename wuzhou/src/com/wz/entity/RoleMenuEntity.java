package com.wz.entity;

public class RoleMenuEntity {
	private Integer role_menu_id;
	private RoleEntity roleEntity;
	private MenuEntity menuEntity;
	public Integer getRole_menu_id() {
		return role_menu_id;
	}
	public void setRole_menu_id(Integer role_menu_id) {
		this.role_menu_id = role_menu_id;
	}
	public RoleEntity getRoleEntity() {
		return roleEntity;
	}
	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
	}
	public MenuEntity getMenuEntity() {
		return menuEntity;
	}
	public void setMenuEntity(MenuEntity menuEntity) {
		this.menuEntity = menuEntity;
	}
}
