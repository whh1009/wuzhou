package com.wz.common;

public class PageEntity {
	private int currentPage; //当前页
	private int pageRowCount; //每页显示条数
	private int pageCount; //总页码
	private int rowCount; //总记录数
	public int getCurrentPage() {
		return currentPage;
	}
	/**
	 * 设置当前页
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageRowCount() {
		return pageRowCount;
	}
	/**
	 * 设置每页显示记录数
	 * @param pageRowCount
	 */
	public void setPageRowCount(int pageRowCount) {
		this.pageRowCount = pageRowCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	/**
	 * 设置总页数
	 * @param pageCount
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getRowCount() {
		return rowCount;
	}
	/**
	 * 设置总记录数
	 * @param rowCount
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	/**
	 * 获取总页码
	 * @param pageRowCount 每页显示记录数
	 * @param rowCount 总记录数
	 * @return
	 */
	public int getPageCount(int pageRowCount, int rowCount) {
		//总页码
		int pageCount = 0;
		if(rowCount%pageRowCount==0) { //可以整除
			pageCount = rowCount/pageRowCount;
		} else {
			pageCount = rowCount/pageRowCount + 1;
		}
		return pageCount;
	}
}
