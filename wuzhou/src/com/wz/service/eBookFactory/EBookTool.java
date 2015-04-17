package com.wz.service.eBookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wz.entity.BookEntity;
import com.wz.entity.UserEntity;
import com.wz.util.FileUtil;

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
	 * @param be 对象
	 */
	public static void copy(String srcPath, String desPath, String extension, String name) {
		List<File> mobiList = new ArrayList<File>();
		EBookTool.getFileByExtension(srcPath, extension, mobiList);
		for(File file : mobiList) { 
			try {
				FileUtil.copyFile(file, new File(desPath+"\\"+name+"."+extension), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
