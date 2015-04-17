package com.wz.entity;

public class BookOnlineEntity {
	private Integer online_id;
	private Integer book_id;
	private Integer os_id;
	private Integer is_online;
	private String online_desc;
	public Integer getOnline_id() {
		return online_id;
	}
	public void setOnline_id(Integer online_id) {
		this.online_id = online_id;
	}
	public Integer getBook_id() {
		return book_id;
	}
	public void setBook_id(Integer book_id) {
		this.book_id = book_id;
	}
	public Integer getOs_id() {
		return os_id;
	}
	public void setOs_id(Integer os_id) {
		this.os_id = os_id;
	}
	public Integer getIs_online() {
		return is_online;
	}
	public void setIs_online(Integer is_online) {
		this.is_online = is_online;
	}
	public String getOnline_desc() {
		return online_desc;
	}
	public void setOnline_desc(String online_desc) {
		this.online_desc = online_desc;
	}
}
