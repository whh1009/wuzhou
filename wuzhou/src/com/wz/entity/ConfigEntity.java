package com.wz.entity;

public class ConfigEntity {
	private Integer config_id;
	private Integer user_id;
	private String show_column;
	private String export_column;
	private String search_column;
	public Integer getConfig_id() {
		return config_id;
	}
	public void setConfig_id(Integer config_id) {
		this.config_id = config_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getShow_column() {
		return show_column;
	}
	public void setShow_column(String show_column) {
		this.show_column = show_column;
	}
	public String getExport_column() {
		return export_column;
	}
	public void setExport_column(String export_column) {
		this.export_column = export_column;
	}
	public String getSearch_column() {
		return search_column;
	}
	public void setSearch_column(String search_column) {
		this.search_column = search_column;
	}
	
}
