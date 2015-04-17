package com.wz.entity;

import java.io.Serializable;

/**
 * 元数据实体类
 * @author wanghonghui
 *
 */
@SuppressWarnings("serial")
public class YuanDataEntity implements Serializable{
	private String isbn;
	private String name;
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
