package com.wz.ftp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wz.common.ConfigInfo;
import com.wz.util.StringUtil;
/**
 * 
 * 根据数据库创建ftp权限配置xml
 */
public class FTPName {

	static String url = "jdbc:mysql://localhost:3306/wuzhou";
	static String username = "root";
	static String password = "123456";
	
	static Connection conn = null;
	static ResultSet rs = null;
	
	public static void getConn() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url, username, password);
	}
	
	/**
	 * 
	 * <User Name="sunlei">
            <Option Name="Pass">6f0b41a05e08df8049ad27311a95c52b</Option>
            <Option Name="Group"></Option>
            <Option Name="Bypass server userlimit">0</Option>
            <Option Name="User Limit">0</Option>
            <Option Name="IP Limit">0</Option>
            <Option Name="Enabled">1</Option>
            <Option Name="Comments"></Option>
            <Option Name="ForceSsl">0</Option>
            <Option Name="8plus3">0</Option>
            <IpFilter>
                <Disallowed />
                <Allowed />
            </IpFilter>
            <Permissions>
                <Permission Dir="D:\ftp\sunlei">
                    <Option Name="FileRead">1</Option>
                    <Option Name="FileWrite">1</Option>
                    <Option Name="FileDelete">0</Option>
                    <Option Name="FileAppend">0</Option>
                    <Option Name="DirCreate">1</Option>
                    <Option Name="DirDelete">1</Option>
                    <Option Name="DirList">1</Option>
                    <Option Name="DirSubdirs">1</Option>
                    <Option Name="IsHome">1</Option>
                    <Option Name="AutoCreate">0</Option>
                </Permission>
            </Permissions>
            <SpeedLimits DlType="0" DlLimit="10" ServerDlLimitBypass="0" UlType="0" UlLimit="10" ServerUlLimitBypass="0">
                <Download />
                <Upload />
            </SpeedLimits>
        </User>
	 * @param sql
	 * @throws java.sql.SQLException
	 * @throws ClassNotFoundException
	 */
	public static void getResult(String sql) throws SQLException, ClassNotFoundException {
		rs = conn.prepareStatement(sql).executeQuery();
		String out = "<User Name=\"admin\"><Option Name=\"Pass\">"+StringUtil.MD5("admin")+"</Option><Option Name=\"Group\"></Option><Option Name=\"Bypass server userlimit\">0</Option><Option Name=\"User Limit\">0</Option><Option Name=\"IP Limit\">0</Option><Option Name=\"Enabled\">1</Option><Option Name=\"Comments\"></Option><Option Name=\"ForceSsl\">0</Option><Option Name=\"8plus3\">0</Option><IpFilter><Disallowed /><Allowed /></IpFilter>" +
					"<Permissions><Permission Dir=\""+ConfigInfo.FTP_ROOT+"\"><Option Name=\"FileRead\">1</Option><Option Name=\"FileWrite\">1</Option><Option Name=\"FileDelete\">1</Option><Option Name=\"FileAppend\">1</Option><Option Name=\"DirCreate\">1</Option><Option Name=\"DirDelete\">1</Option><Option Name=\"DirList\">1</Option><Option Name=\"DirSubdirs\">1</Option><Option Name=\"IsHome\">1</Option><Option Name=\"AutoCreate\">0</Option></Permission></Permissions><SpeedLimits DlType=\"0\" DlLimit=\"10\" ServerDlLimitBypass=\"0\" UlType=\"0\" UlLimit=\"10\" ServerUlLimitBypass=\"0\"><Download /><Upload /></SpeedLimits></User>";
		while(rs.next()) {
			String userName = rs.getString("user_name");
			String pass = rs.getString("user_pwd");
			if("wuzhou".equals(userName)) {
				out = out + "<User Name=\""+userName+"\"><Option Name=\"Pass\">"+StringUtil.MD5(userName)+"</Option><Option Name=\"Group\"></Option><Option Name=\"Bypass server userlimit\">0</Option><Option Name=\"User Limit\">0</Option><Option Name=\"IP Limit\">0</Option><Option Name=\"Enabled\">1</Option><Option Name=\"Comments\"></Option><Option Name=\"ForceSsl\">0</Option><Option Name=\"8plus3\">0</Option><IpFilter><Disallowed /><Allowed /></IpFilter>" +
						"<Permissions><Permission Dir=\""+ConfigInfo.FTP_ROOT+"\"><Option Name=\"FileRead\">1</Option><Option Name=\"FileWrite\">1</Option><Option Name=\"FileDelete\">1</Option><Option Name=\"FileAppend\">1</Option><Option Name=\"DirCreate\">1</Option><Option Name=\"DirDelete\">1</Option><Option Name=\"DirList\">1</Option><Option Name=\"DirSubdirs\">1</Option><Option Name=\"IsHome\">1</Option><Option Name=\"AutoCreate\">0</Option></Permission></Permissions><SpeedLimits DlType=\"0\" DlLimit=\"10\" ServerDlLimitBypass=\"0\" UlType=\"0\" UlLimit=\"10\" ServerUlLimitBypass=\"0\"><Download /><Upload /></SpeedLimits></User>";
			} else {
				out = out + "<User Name=\""+userName+"\">" +
						"<Option Name=\"Pass\">"+StringUtil.MD5(userName)+"</Option><" +
						"Option Name=\"Group\"></Option><Option Name=\"Bypass server userlimit\">0</Option>" +
						"<Option Name=\"User Limit\">0</Option><Option Name=\"IP Limit\">0</Option>" +
						"<Option Name=\"Enabled\">1</Option><Option Name=\"Comments\"></Option>" +
						"<Option Name=\"ForceSsl\">0</Option><Option Name=\"8plus3\">0</Option>" +
						"<IpFilter><Disallowed /><Allowed /></IpFilter>" +
							"<Permissions>" +
							"<Permission Dir=\""+ConfigInfo.FTP_ROOT+"\\"+userName+"\">" +
							"<Option Name=\"FileRead\">1</Option>" +
							"<Option Name=\"FileWrite\">1</Option>" +
							"<Option Name=\"FileDelete\">1</Option>" +
							"<Option Name=\"FileAppend\">0</Option>" +
							"<Option Name=\"DirCreate\">1</Option>" +
							"<Option Name=\"DirDelete\">1</Option>" +
							"<Option Name=\"DirList\">1</Option>" +
							"<Option Name=\"DirSubdirs\">1</Option>" +
							"<Option Name=\"IsHome\">1</Option>" +
							"<Option Name=\"AutoCreate\">1</Option>" +
							"</Permission>" +
							/*
							"<Permission Dir=\""+ConfigInfo.FTP_ROOT+"\\201409之前书目\">"+
			                    "<Aliases>"+
			                        "<Alias>/公共书目</Alias>"+
			                    "</Aliases>"+
			                    "<Option Name=\"FileRead\">1</Option>"+
			                    "<Option Name=\"FileWrite\">0</Option>"+
			                    "<Option Name=\"FileDelete\">0</Option>"+
			                    "<Option Name=\"FileAppend\">0</Option>"+
			                    "<Option Name=\"DirCreate\">0</Option>"+
			                    "<Option Name=\"DirDelete\">0</Option>"+
			                    "<Option Name=\"DirList\">1</Option>"+
			                    "<Option Name=\"DirSubdirs\">1</Option>"+
			                    "<Option Name=\"IsHome\">0</Option>"+
			                    "<Option Name=\"AutoCreate\">0</Option>"+
			                "</Permission>"+
			                */
						"</Permissions>" +
						"<SpeedLimits DlType=\"0\" DlLimit=\"10\" ServerDlLimitBypass=\"0\" UlType=\"0\" UlLimit=\"10\" ServerUlLimitBypass=\"0\"><Download /><Upload /></SpeedLimits></User>";
			}
		}
		System.out.println(out);
	}

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws java.sql.SQLException
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		getConn();
		getResult("select * from wz_user");
		close();
	}
	
	
	
	
	public static void close() throws SQLException {
		if(rs!=null) {
			rs.close();
		}
		if(conn!=null) {
			conn.close();
		}
	}
}
