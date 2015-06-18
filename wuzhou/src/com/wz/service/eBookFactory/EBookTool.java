package com.wz.service.eBookFactory;

import com.wz.entity.UserEntity;
import com.wz.util.FileUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EBookTool {
	/**
	 * 递归遍历文件或文件夹获取指定后缀的文件
	 * @param parentFold
	 * @param extension
	 * @param fileList
	 */
	public static void getFileByExtension(String parentFold, String extension, List<File> fileList) {
		File[] files = new File(parentFold).listFiles();
		if(files==null||files.length==0)return;
		for(File file : files) {
			if(file.isDirectory()) {
				getFileByExtension(file.getAbsolutePath(), extension, fileList);
			} else {
				String fileName = file.getName().toLowerCase();
				String type = fileName.substring(fileName.lastIndexOf(".")+1);
				if(type.equals(extension)) {
					fileList.add(file);
				}
			}
		}
	}
	
	/**
	 * 根据用户id，获取用户名
	 * @param userId
	 * @param userList
	 * @return
	 */
	public static String getUserNameByUserId(int userId, List<UserEntity> userList) {
		if(userList==null||userList.isEmpty()) return "";
		String userName = "";
		for(UserEntity ue : userList) {
			if(userId == ue.getUser_id()) {
				userName = ue.getUser_name();
				break;
			}
		}
		return userName;
	}
	
	/**
	 *  复制文件
	 * @param srcPath 原路径
	 * @param desPath 目的路径
	 * @param extension 后缀
	 * @param name 文件名
	 */
	public static void copy(String srcPath, String desPath, String extension, String name) {
		List<File> fileList = new ArrayList<File>();
		EBookTool.getFileByExtension(srcPath, extension, fileList);
		if(fileList==null||fileList.isEmpty()) return;
		for(File file : fileList) {
			try {
				FileUtil.copyFile(file, new File(desPath+"\\"+name+"."+extension), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 基本单元格，样式四周边框
	 * @param wb
	 * @param row
	 * @param cellNum
	 * @param cellValue
	 * @param flag 标记是否需要右对齐 1 右对齐
	 */
	public static void createBaseCell(XSSFWorkbook wb,XSSFRow row, int cellNum, String cellValue, int flag) {
		XSSFCell cell = row.createCell(cellNum);
		cell.setCellValue(cellValue);
		CellStyle style = wb.createCellStyle();
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		if(flag==1) {
			style.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
		}
		cell.setCellStyle(style);
	}
}
