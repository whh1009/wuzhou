package com.wz.test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.wz.entity.FTPFileEntity;
import com.wz.util.FileUtil;
import com.wz.util.StringUtil;

public class Test {

	public static void main(String[] args) throws Exception{
//		List<String> l = new ArrayList<String>();
//		for(String str : l) {
//			System.out.println(str);
//		}
//		System.out.println("aA".replace("A", "B").replace("a", "b"));
		
//		copyFold("","");
		String str = "e:\\ftp\\cicc\\B_EP_9787508526881_003_aA2Cu";
		File file = new File(str);
		if(file.exists()) {
			System.out.println("1");
		} else {
			System.out.println("-1");
		}
	}
	
	public static void copyFold(String srcFold, String disFold) {
		String srcDirName = "I:/cpdocbook_v2.0.0";
		String destDirName = "H:/cpdocbook_v2.0.0";
		boolean overwrite = true;
		try {
			FileUtil.copyFiles(srcDirName, destDirName, overwrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void subFile(String filePath, List<FTPFileEntity> list) throws Exception {
		File [] file = new File(filePath).listFiles();
		for(File f: file) {
			if(f.isFile()) {
				FTPFileEntity ftpFile = new FTPFileEntity();
				ftpFile.setAbsolutePath(f.getAbsolutePath());
				ftpFile.setSize(f.length()+"");
				list.add(ftpFile);
			} else {
				subFile(f.getAbsolutePath(), list);
			}
		}
	}
}
