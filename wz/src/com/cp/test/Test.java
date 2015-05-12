package com.cp.test;

import java.io.File;

import com.cp.util.FileUtil;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File [] files = new File("E:\\wanghonghui\\c词典").listFiles();
		for(File file : files) {
			if(file.getName().endsWith("_gai.xml")) {
				String str = FileUtil.fileInput(file.getAbsolutePath(), "utf-8");
				if(str.contains("<Pron><![CDATA[")) {
					System.out.println(file.getName());
				}
			}
		}

	}

}
