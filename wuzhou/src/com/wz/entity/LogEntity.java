package com.wz.entity;

import java.util.Date;

public class LogEntity {
	private Integer modify_id;
	private String book_serial_number;
	private String user_name;
	private Date modify_time;
	private String modify_type;
	public Integer getModify_id() {
		return modify_id;
	}
	public void setModify_id(Integer modify_id) {
		this.modify_id = modify_id;
	}
	
	public String getBook_serial_number() {
		return book_serial_number;
	}
	public void setBook_serial_number(String book_serial_number) {
		this.book_serial_number = book_serial_number;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public String getModify_type() {
		return modify_type;
	}
	public void setModify_type(String modify_type) {
		this.modify_type = modify_type;
	}
}
