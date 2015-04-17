package com.wz.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.wz.util.FileUtil;

public class Test3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		String []line = fileInput("E:\\wanghonghui\\workspace2013\\wuzhou\\src\\com\\wz\\test\\txt.txt", "utf-8").split("@@@");
		
		
		String fold = "H:\\20150330";
		String outFold = "J:\\whh\\";
		File [] files = new File(fold).listFiles();
		int i = 0;
		for(File file: files) {
			String isbn4 = file.getName().substring(0,4);
			i++;
			
			boolean flag = false;
			for(String str : line) {
				String isbn = str.split("\t")[0].substring(8,12);
				if(isbn.equals(isbn4)) {
					flag = true;
					String userName = str.split("\t")[13];
					/*
					System.out.println(file.getName());
					String srcFold = file.getAbsolutePath()+"\\1. 合同";
					String destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\合同";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\2. 出版书文件\\InDesign";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\排版\\InDesign";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\2. 出版书文件\\PageMaker";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\排版\\PageMaker";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\2. 出版书文件\\PDF";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\分层PDF";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\2. 出版书文件\\其他";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\排版\\其他";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\3. 电子书文件\\EPUB";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\EPUB";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\3. 电子书文件\\MOBI";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\MOBI";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\3. 电子书文件\\XML";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\XML";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\3. 电子书文件\\过程文件";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\电子书过程文件";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\3. 电子书文件\\PDF";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\阅读PDF";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\4. Thats网站\\封面";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\封面";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					srcFold = file.getAbsoluteFile()+"\\4. Thats网站\\样章";
					destFold = outFold+userName+"\\"+str.split("\t")[1]+"\\样章";
					FileUtil.copyFiles(srcFold, destFold, true);
					
					new File(outFold+userName+"\\"+str.split("\t")[1]+"\\WORD").mkdirs();
					new File(outFold+userName+"\\"+str.split("\t")[1]+"\\HTML").mkdirs();
					new File(outFold+userName+"\\"+str.split("\t")[1]+"\\WORD").mkdirs();
					new File(outFold+userName+"\\"+str.split("\t")[1]+"\\字体").mkdirs();
					*/
					break;
				} else {
					flag = false;
				}
			}
			if(!flag) {
				System.out.println("--"+file.getName());
			}
//			if(i==5) {
//				break;
//			}
		}
		
	}
	
	public static String fileInput(String srcPath, String charset) throws IOException {
		File file = new File(srcPath);
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(stream, charset);
		BufferedReader br = new BufferedReader(isr);
		String temp = null;
		StringBuffer sb = new StringBuffer();
		while ((temp = br.readLine()) != null) {
			sb.append(temp).append("@@@");
		}
		br.close();
		isr.close();
		stream.close();
		return sb.toString();
	}
	

}
