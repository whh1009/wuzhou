package com.wz.entity;

public class FTPFileEntity {
	String LocalFile;
	String RemoteFile;
	String RemotePath;
	String Download;
	String Size;
	String DataType;
	String absolutePath;
	public String getLocalFile() {
		return LocalFile;
	}
	public void setLocalFile(String localFile) {
		LocalFile = localFile;
	}
	public String getRemoteFile() {
		return RemoteFile;
	}
	public void setRemoteFile(String remoteFile) {
		RemoteFile = remoteFile;
	}
	public String getRemotePath() {
		return RemotePath;
	}
	public void setRemotePath(String remotePath) {
		RemotePath = remotePath;
	}
	public String getDownload() {
		return Download;
	}
	public void setDownload(String download) {
		Download = download;
	}
	public String getSize() {
		return Size;
	}
	public void setSize(String size) {
		Size = size;
	}
	public String getDataType() {
		return DataType;
	}
	public void setDataType(String dataType) {
		DataType = dataType;
	}
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
}
