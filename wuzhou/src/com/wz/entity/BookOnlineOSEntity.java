package com.wz.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BookOnlineOSEntity implements Serializable{
	private Integer os_id;
	private String os_name;
	private String os_desc;
	private Integer os_flag;
	public Integer getOs_id() {
		return os_id;
	}
	public void setOs_id(Integer os_id) {
		this.os_id = os_id;
	}
	public String getOs_name() {
		return os_name;
	}
	public void setOs_name(String os_name) {
		this.os_name = os_name;
	}
	public String getOs_desc() {
		return os_desc;
	}
	public void setOs_desc(String os_desc) {
		this.os_desc = os_desc;
	}
	public Integer getOs_flag() {
		return os_flag;
	}
	public void setOs_flag(Integer os_flag) {
		this.os_flag = os_flag;
	}
	
}
