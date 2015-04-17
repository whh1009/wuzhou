package com.wz.test;

import java.io.File;

import com.wz.util.FileUtil;

public class Test5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String srcPath = "\\\\172.16.1.121\\资源共享\\语料\\2009\\北京文学·中篇小说月报_2009_1";
		srcPath = srcPath + "\\catalog";
		File [] files = new File(srcPath).listFiles();
		for(File file : files) {
			if("catalog.xml".equals(file.getName())) {
			}
		}

	}

}
