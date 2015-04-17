package net.szh.zip;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class ZipTest {

	
	public static void zipFiles(String zipPath, String srcPathName) {
		File srcdir = new File(srcPathName);
		if (!srcdir.exists())
			throw new RuntimeException(srcPathName + "不存在！");
		
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(new File(zipPath));
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		//fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
		//fileSet.setExcludes(...); 排除哪些文件或文件夹
		zip.addFileset(fileSet);
		
		zip.execute();
	}
	
	public static void main(String[] args) {
//		ZipTest zc = new  ZipTest("f:\\我.docx");
//        zc.compress("f:\\b\\out");
        
//        ZipTest zca = new ZipTest("E:\\szhzipant.zip");
//        zca.compress("E:\\test");
		
		zipFiles("E:\\szhzipant.zip", "f:\\b\\out");
    }
}