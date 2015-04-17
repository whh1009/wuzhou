package com.wz.service.eBookFactory;

import java.io.OutputStream;
import java.util.List;

import com.wz.entity.BookEntity;
import com.wz.entity.UserEntity;

/**
 * 
 * @author wanghonghui
 *
 */
public interface EBookFormat {
	
	/**
	 * 重命名
	 * @param bookList 图书列表
	 * @param desPath 目的地路径
	 * @param excelPath 元数据路径
	 */
	public void renameEBook(List<BookEntity> bookList, List<UserEntity> userList, String desPath, String excelPath);
	
	/**
	 * 创建excel
	 * @param out 输出流
	 */
	public void createExcel(OutputStream out);
}
