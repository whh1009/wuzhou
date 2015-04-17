package com.wz.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport {
	private String fileName;
	private InputStream fileInputStream;
	private File upload;
	private String uploadContentType;
	private String uploadFileName;
	private String savePath;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
	public String upLoad() throws Exception {
		String fileName = getSavePath() + "/" + getUploadFileName();
		FileOutputStream fos = new FileOutputStream(fileName);
		FileInputStream fis = new FileInputStream(getUpload());
		byte[] b = new byte[10240];
		int len = 0;
		while((len=fis.read(b))>0) {
			fos.write(b,0,len);
		}
		fis.close();
		fos.close();
		return Action.SUCCESS;
	}
	
	public String fileDownload() throws Exception {
		fileName="workbook.xlsx";
		fileInputStream = new FileInputStream(new File("D:\\workbook.xlsx"));
		return "success";
	}
}
