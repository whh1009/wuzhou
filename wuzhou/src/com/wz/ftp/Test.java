package com.wz.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Test {

	public static void main(String[] args) {
		testUpload();
		
	}
	
	/** 
    * FTP上传单个文件测试 
    */ 
   public static void testUpload() { 
       FTPClient ftpClient = new FTPClient(); 
       FileInputStream fis = null; 
       try { 
           ftpClient.connect("172.16.5.31"); 
           ftpClient.login("sunlei", "sunlei"); 
           File srcFile = new File("f:\\loadrunner-11.iso"); 
           fis = new FileInputStream(srcFile); 
           //设置上传目录 
           ftpClient.changeWorkingDirectory("d:\\aaa\\"); 
           ftpClient.setBufferSize(1024); 
           ftpClient.setControlEncoding("utf-8"); 
           //设置文件类型（二进制） 
           ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
           ftpClient.storeFile("3.iso", fis); 
       } catch (IOException e) { 
           e.printStackTrace(); 
           throw new RuntimeException("FTP客户端出错！", e); 
       } finally { 
           IOUtils.closeQuietly(fis); 
           try { 
               ftpClient.disconnect(); 
           } catch (IOException e) { 
               e.printStackTrace(); 
               throw new RuntimeException("关闭FTP连接发生异常！", e); 
           } 
       } 
   } 

	/** 
     * FTP下载单个文件测试 
     */ 
    public static void testDownload() { 
        FTPClient ftpClient = new FTPClient(); 
        FileOutputStream fos = null; 

        try { 
            ftpClient.connect("192.168.1.6"); 
            ftpClient.login("admin", "123"); 

            String remoteFileName = "/admin/pic/3.gif"; 
            fos = new FileOutputStream("c:/down.gif"); 

            ftpClient.setBufferSize(1024); 
            //设置文件类型（二进制） 
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftpClient.retrieveFile(remoteFileName, fos); 
        } catch (IOException e) { 
            e.printStackTrace(); 
            throw new RuntimeException("FTP客户端出错！", e); 
        } finally { 
            IOUtils.closeQuietly(fos); 
            try { 
                ftpClient.disconnect(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
                throw new RuntimeException("关闭FTP连接发生异常！", e); 
            } 
        } 
    } 
}
