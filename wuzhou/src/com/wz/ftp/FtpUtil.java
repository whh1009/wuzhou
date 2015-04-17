package com.wz.ftp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.wz.entity.FTPFileEntity;

public class FtpUtil {
	private FTPClient ftpClient;
	private String host;
	private String userName;
	private String pwd;
	private int port;
	public FtpUtil(String host, String userName, String pwd, int port) {
		this.host = host;
		this.userName = userName;
		this.pwd = pwd;
		this.port = port;
	}
	
	public FtpUtil() {
	}

	/**
	 * 登录ftp
	 * @return
	 */
	public boolean ftpLogin() {
		boolean isLogin = false;
		ftpClient = new FTPClient();
		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
		ftpClient.setControlEncoding("utf-8");
		ftpClient.configure(ftpClientConfig);
		try {
			ftpClient.connect(getHost(), getPort());
			int reply = ftpClient.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return isLogin;
			}
			ftpClient.login(getUserName(), getPwd());
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			isLogin = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return isLogin;
	}
	
	
	/**
	 * 断开FTP连接
	 */
	public void ftpLogout() {
		if (ftpClient != null & ftpClient.isConnected()) {
			try {
				ftpClient.logout();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					ftpClient.disconnect();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public boolean createFold(String fold) {
//		boolean isLogin = connectFtp(ftpConfigXml);
//		if(!isLogin) { //登录FTP失败
//			return 0;
//		} else {
		boolean flag = false;
			try {
				if(!"".equals(fold)) {
					flag = ftpClient.makeDirectory(fold);
				}
			} catch (IOException e) {
				System.out.println("创建FTP目录异常");
				e.printStackTrace();
			}
//		}
		return flag;
	}
	
	/**
	 * 得到指定路径下的文件列表
	 * @param dir
	 * @return
	 */
	public List<FTPFile> getListFile(String dir) {
		List<FTPFile> fileList = null;
		try {
			fileList = Arrays.asList(ftpClient.listFiles(dir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileList;
	}
	
	public boolean connectFtp(String ftpConfigPath) {
		SAXReader read = new SAXReader();
		boolean isLogin = false;
		Document doc;
		try {
			doc = read.read(new File(ftpConfigPath));
			String host = doc.selectSingleNode("//FileZilla3/Queue/Server/Host").getText();
			String userName = doc.selectSingleNode("//FileZilla3/Queue/Server/User").getText();
			String pwd = doc.selectSingleNode("//FileZilla3/Queue/Server/Pass").getText();
			String port = doc.selectSingleNode("//FileZilla3/Queue/Server/Port").getText();
			setHost(host);
			setUserName(userName);
			setPwd(pwd);
			setPort(Integer.parseInt(port));
			isLogin = ftpLogin();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return isLogin;
	}
	
	/**
	 * 给定ftp目录和配置文件，获取该目录下的文件
	 * @param ftpConfigPath
	 * @param dir
	 * @return
	 */
	public List<FTPFileEntity> getFtpFiles(String dir) {
		List<FTPFileEntity> fileList = new ArrayList<FTPFileEntity>();
		getFile(dir, fileList);
		return fileList;
	}
	
	public static void main(String [] args) throws IOException {
		String configPath = "E:\\wanghonghui\\Workspaces\\MyEclipse Professional\\wuzhou\\WebRoot\\xml\\FTPConfig.xml";
		FtpUtil ftp = new FtpUtil();
		ftp.connectFtp(configPath);
//		List<FTPFileEntity> list = ftp.getFtpFiles("/whh/P_23423443_006_C9JR2/排版");
//		for(FTPFileEntity f : list) {
//			System.out.println(f.getAbsolutePath()+"--"+f.getSize());
//		}
		String fileDir = "/xuxs/EP_7508507002_001_F1AkU_世界反法西斯战争中的中国/排版";
		ftp.ftpClient.makeDirectory(fileDir);
		ftp.ftpLogout();
	}
	
	/**
	 * 递归，把所有文件放到list里
	 * @param ftp
	 * @param dir
	 * @param fileList
	 */
	public void getFile(String dir, List<FTPFileEntity> fileList) {
		List<FTPFile> files = getListFile(dir);
		if(files!=null) {
			for(FTPFile file : files) {
				if(file.isFile()) {
//					System.out.println(dir+"/"+file.getName());
					FTPFileEntity f = new FTPFileEntity();
					f.setAbsolutePath(dir+"/"+file.getName());
					f.setSize(file.getSize()+"");
					fileList.add(f);
				} else {
					getFile(dir+"/"+file.getName(), fileList);
				}
			}
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
