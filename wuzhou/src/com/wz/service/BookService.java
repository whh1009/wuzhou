package com.wz.service;

import com.wz.common.ColumnMap;
import com.wz.common.ConfigInfo;
import com.wz.dao.BookDao;
import com.wz.entity.BookEntity;
import com.wz.entity.BookLanDoughnutChartEntity;
import com.wz.entity.BookResourceEntity;
import com.wz.entity.FTPFileEntity;
import com.wz.ftp.FtpUtil;
import com.wz.util.FileUtil;
import com.wz.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BookService {
	public static Logger log = Logger.getLogger("wuzhou");
	private BookDao bookDao;

	public BookService() {
		bookDao = new BookDao();
	}

	/**
	 * 查询所有book
	 * 
	 * @param hql
	 * @return
	 */
	public List<BookEntity> getBookListByHql(String hql) throws Exception{
		return bookDao.getBookListByHql(hql);
	}

	/**
	 * 分页显示符合所有的记录数
	 * 
	 * @param currentPage
	 *            当前页码
	 * @param pageRowCount
	 *            每页显示的条数
	 * @param hql
	 * @return
	 */
	public List<BookEntity> findPageFromBook(int currentPage, int pageRowCount, String hql)throws Exception {
		return bookDao.findPageByHqlFromBook(currentPage, pageRowCount, hql);
	}

	/**
	 * 根据hql获取总页码
	 * 
	 * @param hql
	 * @return
	 */
	public int getPageCount(String hql, int pageRowCount) throws Exception{
		// 总记录
		int rowCount = bookDao.getCountByHqlFromBook(hql);
		// 总页码
		int pageCount = 0;
		if (rowCount % pageRowCount == 0) { // 可以整除
			pageCount = (rowCount / pageRowCount);
		} else {
			pageCount = rowCount / pageRowCount + 1;
		}
		return pageCount;
	}

	/**
	 * 根据hql获取总记录数
	 * 
	 * @param hql
	 * @return
	 */
	public int getRowCount(String hql) throws Exception{
		return bookDao.getCountByHqlFromBook(hql);
	}

	/**
	 * 添加图书
	 * 
	 * @param bookEntity
	 * @return
	 */
	public int addBook(BookEntity bookEntity) throws Exception{
		return bookDao.addBook(bookEntity);
	}

	/**
	 * 添加“其他”新书
	 * 
	 * @param bsn
	 * @return
	 */
	public int addBookByBsn(String bsn, int userId) throws Exception{
		return bookDao.addBookByBsn(bsn, userId);
	}

	/**
	 * 保存图书
	 * 
	 * @param bookEntity
	 * @return
	 */
	public int updateBook(BookEntity bookEntity)throws Exception {
		return bookDao.updateBook(bookEntity);
	}

	/**
	 * 删除图书
	 * 
	 * @param bookId
	 * @return 成功返回1，失败返回0
	 */
	public int deleteBook(Integer bookId) throws Exception{
		// return bookDao.deleteBook(bookId);
		String sql = "update wz_book set book_del_flag = 1 where book_id = " + bookId;
		return bookDao.updateBySql(sql);
	}

	/**
	 * 根据图书ID获取图书信息
	 * 
	 * @param bookId
	 * @return
	 */
	public BookEntity getBookEntityById(Integer bookId) throws Exception{
		return bookDao.getBookEntityById(bookId);
	}

	/**
	 * 生成excel
	 * 
	 * @param filePath
	 *            文件存放路径 f:\\test\
	 * @param fileName
	 *            excel名称
	 * @param bookList
	 * @param exportColumn
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws java.io.IOException
	 */
	public void createExcel(String filePath, String fileName, List<BookEntity> bookList, String exportColumn) throws Exception {
		// 删除已有的excel
		deleteExistFile(filePath);
		Workbook wb = new XSSFWorkbook();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Sheet helloSheet = wb.createSheet("Sheet1");
		Row row1 = helloSheet.createRow(0);
		row1.setRowStyle(setTitleStyle(wb));
		String[] exportColumnArray = exportColumn.split(",");
		for (int i = 0; i < exportColumnArray.length; i++) {
			row1.createCell(i).setCellValue(ColumnMap.getBookTableCnByColumnName(exportColumnArray[i].trim()));
		}
		for (int i = 0; i < bookList.size(); i++) {
			BookEntity be = bookList.get(i);
			Field[] fields = be.getClass().getFields();
			Row row = helloSheet.createRow(i + 1);
			for (int j = 0; j < exportColumnArray.length; j++) {
				for (Field f : fields) {
					if (exportColumnArray[j].equals(f.getName())) {
						row.createCell(j).setCellValue(StringUtil.ObjectToString(f.get(be)));
						break;
					}
				}
			}
		}
		wb.write(fos);
		fos.flush();
		fos.close();
	}

	/**
	 * 
	 * @param jsonString
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public void json2Excel(String jsonString, String filePath, String fileName, String exportColumn) throws Exception {
		// 删除已有的excel
		deleteExistFile(filePath);
		JSONObject jsonObj = JSONObject.fromObject(jsonString);
		JSONArray jsonArr = jsonObj.getJSONArray("bookList");
		// Map<String, String> map = ColumnMap.initBookTableColumnMap();
		Workbook wb = new XSSFWorkbook();
		FileOutputStream fos = new FileOutputStream(filePath + fileName);
		Sheet helloSheet = wb.createSheet("Sheet1");
		Row row1 = helloSheet.createRow(0);
		row1.setRowStyle(setTitleStyle(wb));
		String[] exportColumnArray = exportColumn.split(",");
		for (int i = 0; i < exportColumnArray.length; i++) {
			row1.createCell(i).setCellValue(ColumnMap.getBookTableCnByColumnName(exportColumnArray[i].trim()));
		}
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject jo = (JSONObject) jsonArr.get(i);
			Row row = helloSheet.createRow(i + 1);
			for (int j = 0; j < exportColumnArray.length; j++) {
				row.createCell(j).setCellValue(jo.getString(exportColumnArray[j]));
			}
		}
		wb.write(fos);
		fos.flush();
		fos.close();
	}

	/**
	 * 设置标题样式 ：
	 * 
	 * 水平居中，垂直居中，黑体 加粗 16号字
	 * 
	 * @param wb
	 * @return
	 */
	private CellStyle setTitleStyle(Workbook wb)  throws Exception{
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 水平居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
		Font font = wb.createFont();
		font.setFontName("黑体");
		// font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 16);
		cellStyle.setFont(font);
		return cellStyle;
	}

	/**
	 * 删除目录下的所有的文件
	 * 
	 * @param filePath
	 */
	private void deleteExistFile(String filePath)  throws Exception{
		// System.out.println(filePath);
		File[] files = new File(filePath).listFiles();
		if(files==null) return;
		for (File file : files) {
			file.delete();
		}
	}

	/**
	 * 处理版权过期的图书 版权过期的设置为1，没有过期的设置为0；
	 * 
	 * @param bookId
	 * @return
	 */
	public int handleCopyrightExpires(int bookId) throws Exception{
		String sql = "update wz_book set book_flag =1 where book_id = " + bookId;
		return bookDao.updateBySql(sql);
	}

	/**
	 * 恢复已删除的图书
	 * 
	 * @param bookId
	 * @return
	 */
	public int recoveryDelBook(int bookId) throws Exception{
		String sql = "update wz_book set book_del_flag =0 where book_id = " + bookId;
		return bookDao.updateBySql(sql);
	}

	/**
	 * 真实删除图书
	 * 
	 * @param bookId
	 * @return
	 */
	public int realDelBook(int bookId) throws Exception{
		String sql = "delete from wz_book where book_id = " + bookId;
		return bookDao.updateBySql(sql);
	}

	/**
	 * 添加引用 根据图书类型和图书编号找到另一本图书
	 * 
	 * @param bookType
	 * @return
	 */
	public BookEntity getBookEntityByISBN(String book_isbn, int bookType) throws Exception{
		// String hql =
		// "from BookEntity where book_type = "+bookType+" and book_serial_number like '%\\_"+book_serial_number+"\\_%'";
		String hql = "from BookEntity where book_isbn = '" + book_isbn.replace("_", "\\_") + "' and book_del_flag = 0";
		List<BookEntity> bookList = bookDao.getBookListByHql(hql);
		if (bookList == null || bookList.size() == 0) {
			return null;
		} else {
			return bookList.get(0);
		}
	}

	/**
	 * 创建上传的xml
	 * 
	 * @param xmlPath
	 * @param booktype
	 * @param neiwenserver
	 * @param neiwenlocal
	 * @param coverserver
	 * @param coverlocal
	 * @param fontserver
	 * @param fontlocal
	 * @param pdfpublishserver
	 * @param pdfpublishlocal
	 * @param wordserver
	 * @param wordlocal
	 * @param xmlserver
	 * @param xmllocal
	 * @param epubserver
	 * @param epublocal
	 * @param mobiserver
	 * @param mobilocal
	 * @param pdfreadserver
	 * @param pdfreadlocal
	 * @param htmlserver
	 * @param htmllocal
	 */
	public void createUploadXml(String xmlPath, String booktype, String xmlName, String neiwenserver, String neiwenlocal, String coverserver, String coverlocal, String fontserver, String fontlocal, String pdfpublishserver, String pdfpublishlocal, String wordserver, String wordlocal,
			String xmlserver, String xmllocal, String epubserver, String epublocal, String mobiserver, String mobilocal, String pdfreadserver, String pdfreadlocal, String htmlserver, String htmllocal)  throws Exception{
		delFile(xmlPath); // 删除已经下载的文件
		SAXReader read = new SAXReader();
		try {
			Document doc = read.read(new File(xmlPath + "FTPConfig.xml"));
			Element serverEle = (Element) doc.selectSingleNode("//Server");
			// 纸书

			File neiwenlocalFile = new File(neiwenlocal);
			// if("".equals(neiwenlocal)) neiwenlocalFile=null;
			File neiwenserverFile = new File(neiwenserver);
			// if(!neiwenserverFile.exists()) neiwenserverFile.mkdirs();
			File coverlocalFile = new File(coverlocal);
			// if("".equals(coverlocalFile)) coverlocalFile=null;
			File coverserverFile = new File(coverserver);
			// if(!coverserverFile.exists()) coverserverFile.mkdirs();
			File fontlocalFile = new File(fontlocal);
			// if("".equals(fontlocalFile)) fontlocalFile=null;
			File fontserverFile = new File(fontserver);
			// if(!fontserverFile.exists()) fontserverFile.mkdirs();
			File pdfpublishlocalFile = new File(pdfpublishlocal);
			// if("".equals(pdfpublishlocalFile)) pdfpublishlocalFile=null;
			File pdfpublishserverFile = new File(pdfpublishserver);
			// if(!pdfpublishserverFile.exists()) pdfpublishserverFile.mkdirs();
			File wordlocalFile = new File(wordlocal);
			// if("".equals(wordlocalFile)) wordlocalFile=null;
			File wordserverFile = new File(wordserver);
			// if(!wordserverFile.exists()) wordserverFile.mkdirs();
			// 电子书
			File xmllocalFile = new File(xmllocal);
			// if("".equals(xmllocalFile)) xmllocalFile=null;
			File xmlserverFile = new File(xmlserver);
			// if(!xmlserverFile.exists()) xmlserverFile.mkdirs();
			File epublocalFile = new File(epublocal);
			// if("".equals(epublocalFile)) epublocalFile=null;
			File epubserverFile = new File(epubserver);
			// if(!epubserverFile.exists()) epubserverFile.mkdirs();
			File mobilocalFile = new File(mobilocal);
			// if("".equals(mobilocalFile)) mobilocalFile=null;
			File mobiserverFile = new File(mobiserver);
			// if(!mobiserverFile.exists()) mobiserverFile.mkdirs();
			File pdfreadlocalFile = new File(pdfreadlocal);
			// if("".equals(pdfreadlocalFile)) pdfreadlocalFile=null;
			File pdfreadserverFile = new File(pdfreadserver);
			// if(!pdfreadserverFile.exists()) pdfreadserverFile.mkdirs();
			File htmllocalFile = new File(htmllocal);
			// if("".equals(htmllocalFile)) htmllocalFile=null;
			File htmlserverFile = new File(htmlserver);
			// if(!htmlserverFile.exists()) htmlserverFile.mkdirs();
			if ("1".equals(booktype)) { // 纸书
				addFileEle(serverEle, neiwenlocalFile, neiwenserver);
				addFileEle(serverEle, coverlocalFile, coverserver);
				addFileEle(serverEle, fontlocalFile, fontserver);
				addFileEle(serverEle, pdfpublishlocalFile, pdfpublishserver);
				addFileEle(serverEle, wordlocalFile, wordserver);
			} else if ("2".equals(booktype)) { // 电子书
				addFileEle(serverEle, xmllocalFile, xmlserver);
				addFileEle(serverEle, epublocalFile, epubserver);
				addFileEle(serverEle, mobilocalFile, mobiserver);
				addFileEle(serverEle, pdfreadlocalFile, pdfreadserver);
				addFileEle(serverEle, htmllocalFile, htmlserver);
			} else if ("3".equals(booktype)) { // 纸书和电子书
				addFileEle(serverEle, neiwenlocalFile, neiwenserver);
				addFileEle(serverEle, coverlocalFile, coverserver);
				addFileEle(serverEle, fontlocalFile, fontserver);
				addFileEle(serverEle, pdfpublishlocalFile, pdfpublishserver);
				addFileEle(serverEle, wordlocalFile, wordserver);
				addFileEle(serverEle, xmllocalFile, xmlserver);
				addFileEle(serverEle, epublocalFile, epubserver);
				addFileEle(serverEle, mobilocalFile, mobiserver);
				addFileEle(serverEle, pdfreadlocalFile, pdfreadserver);
				addFileEle(serverEle, htmllocalFile, htmlserver);
			} else { // 其它情况

			}
			try {
				FileUtil.writeFile(doc.asXML(), xmlPath + xmlName, "utf-8");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件，除FTPConfig.xml以外的文件
	 * 
	 * @param xmlPath
	 */
	private void delFile(String xmlPath)  throws Exception{
		File[] dFile = new File(xmlPath).listFiles();
		for (File file : dFile) {
			if (!"FTPConfig.xml".equals(file.getName())) {
				FileUtil.deleteFile(file.getAbsolutePath());
			}
		}
	}

	// 添加File节点
	private void addFileEle(Element serverEle, File localFile, String serverfile)  throws Exception{
		if (localFile != null && !"".equals(serverfile)) {
			List<FTPFileEntity> feList = new ArrayList<FTPFileEntity>();
			getFTPFileList(localFile, feList, "0");
			for (FTPFileEntity fe : feList) {
				Element FileEle = serverEle.addElement("File");
				Element LocalFile = FileEle.addElement("LocalFile");
				LocalFile.setText(fe.getLocalFile());
				Element RemoteFileEle = FileEle.addElement("RemoteFile");
				RemoteFileEle.setText(fe.getRemoteFile());
				Element RemotePathEle = FileEle.addElement("RemotePath");
				String out = "";
				String[] p = serverfile.split("/");
				for (int i = 0; i < p.length; i++) {
					if (!"".equals(p[i])) {
						out = out + " " + p[i].length() + " " + p[i];
					}
				}
				RemotePathEle.setText(("1 0" + out + " " + fe.getRemotePath()).replace("  ", " "));
				Element DownloadEle = FileEle.addElement("Download");
				DownloadEle.setText(fe.getDownload());
				Element SizeEle = FileEle.addElement("Size");
				SizeEle.setText(fe.getSize());
				Element DataTypeEle = FileEle.addElement("DataType");
				DataTypeEle.setText(fe.getDataType());
				Element OverwriteActionEle = FileEle.addElement("OverwriteAction");
				OverwriteActionEle.setText("1");
			}
		}
	}

	/**
	 * 递归生成File 列表
	 * 
	 * @param filePath
	 * @param fileEntity
	 * @param downloadType
	 *            "0"是上传 “1”是下载
	 */
	private void getFTPFileList(File filePath, List<FTPFileEntity> fileEntity, String downloadType) throws Exception {
		if (filePath == null)
			return;
		if (filePath.isFile()) {
			String path = filePath.getAbsolutePath();
			FTPFileEntity fe = new FTPFileEntity();
			fe.setLocalFile(path);
			fe.setRemoteFile(filePath.getName());
			String[] p = path.split("\\\\");
			String remote = "";
			for (int i = 1; i < p.length - 1; i++) {
				remote = remote + " " + p[i].length() + " " + p[i];
			}
			fe.setRemotePath(remote);
			fe.setDownload(downloadType);
			fe.setSize(filePath.length() + "");
			fe.setDataType("1");
			fileEntity.add(fe);
		} else {
			File[] files = filePath.listFiles();
			if (files == null)
				return;
			for (File f : files) {
				getFTPFileList(f, fileEntity, downloadType);
			}
		}
	}

	/**
	 * 生成ftp的xml
	 * 
	 * @param bookList
	 * @param exportXmlCol
	 * @param xmlPath
	 * @param xmlName
	 * @param localpath
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void createFtpXml(List<BookEntity> bookList, String exportXmlCol, String xmlPath, String xmlName, String localpath) throws Exception{
//		FtpUtil ftp = new FtpUtil();
//		boolean isLogin = ftp.connectFtp(xmlPath + "FTPConfig.xml");
//		if (!isLogin)
//			return;
		localpath = localpath.replace("：",":").replace("/", "\\");
		if (!localpath.endsWith("\\")) {
			localpath = localpath + "\\";
		}
		delFile(xmlPath);
		SAXReader read = new SAXReader();
		Document doc = null;
		doc = read.read(new File(xmlPath + "FTPConfig.xml"));
		Element serverEle = (Element) doc.selectSingleNode("//Server");
		exportXmlCol = StringUtil.ignoreComma(exportXmlCol);
		String[] exportColumnArray = exportXmlCol.split(",");
		for (int i = 0; i < bookList.size(); i++) {
			BookEntity be = bookList.get(i);
			Field[] fields = be.getClass().getDeclaredFields();
			for (String col : exportColumnArray) {
				for (Field f : fields) {
					if (col.equals(f.getName())) {
						String serverpath = StringUtil.ObjectToString(f.get(be));
						//FTP目录
//						List<FTPFileEntity> list = ftp.getFtpFiles(serverpath);
//						if (list != null) {
//							for (FTPFileEntity fee : list) {
//								addFileEle(serverEle, localpath, fee);
//							}
//						}
						//本地目录替换FTP目录
						String filePath = ConfigInfo.FTP_ROOT+serverpath.replace("/","\\");
						List<FTPFileEntity> list = new ArrayList<FTPFileEntity>();
						subFile(filePath, list);
						if (list != null) {
							for (FTPFileEntity fee : list) {
								addFileEle(serverEle, localpath, fee);
							}
						}
						break;
					}
				}
			}
		}
		FileUtil.writeFile(doc.asXML(), xmlPath + xmlName, "utf-8");
//		ftp.ftpLogout();
	}
	
	public void subFile(String filePath, List<FTPFileEntity> list) throws Exception {
		File [] file = new File(filePath).listFiles();
		if(file==null) {
			return;
		}
		for(File f: file) {
			if(f.isFile()) {
				FTPFileEntity ftpFile = new FTPFileEntity();
				ftpFile.setAbsolutePath(f.getAbsolutePath().substring(ConfigInfo.FTP_ROOT.length()));
				ftpFile.setSize(f.length()+"");
				list.add(ftpFile);
			} else {
				subFile(f.getAbsolutePath(), list);
			}
		}
	}

	/**
	 * 
	 * @param json
	 * @param exportXmlCol
	 * @param xmlPath
	 * @param xmlName
	 */
	public void createDownloadXml(String json, String exportXmlCol, String xmlPath, String xmlName, String localpath) throws Exception{
		FtpUtil ftp = new FtpUtil();
		boolean isLogin = ftp.connectFtp(xmlPath + "FTPConfig.xml");
		if (!isLogin)
			return;
		localpath = localpath.replace("/", "\\");
		if (!localpath.endsWith("\\")) {
			localpath = localpath + "\\";
		}
		delFile(xmlPath);
		SAXReader read = new SAXReader();
		Document doc = null;
		try {
			doc = read.read(new File(xmlPath + "FTPConfig.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
			return;
		}
		Element serverEle = (Element) doc.selectSingleNode("//Server");
		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONArray jsonArr = jsonObj.getJSONArray("bookList");
		exportXmlCol = StringUtil.ignoreComma(exportXmlCol);
		String[] exportColumnArray = exportXmlCol.split(",");
		for (int i = 0; i < jsonArr.size(); i++) {
			if (i == 3)
				break;
			JSONObject jo = (JSONObject) jsonArr.get(i);
			for (int j = 0; j < exportColumnArray.length; j++) {
				String serverpath = jo.getString(exportColumnArray[j]);
				List<FTPFileEntity> list = ftp.getFtpFiles(serverpath);
				if (list != null) {
					for (FTPFileEntity fee : list) {
						addFileEle(serverEle, localpath, fee);
					}
				}
			}
		}
		try {
			FileUtil.writeFile(doc.asXML(), xmlPath + xmlName, "utf-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ftp.ftpLogout();
	}

	// 添加File节点
	private void addFileEle(Element serverEle, String localpath, FTPFileEntity fee) throws Exception {
		String serverpath = fee.getAbsolutePath();
		if (serverpath == null || serverpath.length() == 0)
			return;
		if (localpath == null || localpath.length() == 0)
			return;
		serverpath = serverpath.substring(1); // 服务器路径的第一个/不要
		Element FileEle = serverEle.addElement("File");
		Element LocalFile = FileEle.addElement("LocalFile");
		LocalFile.setText(localpath + serverpath.replace("/", "\\"));
		Element RemoteFileEle = FileEle.addElement("RemoteFile");
		RemoteFileEle.setText(serverpath.substring(serverpath.lastIndexOf("/") + 1));
		Element RemotePathEle = FileEle.addElement("RemotePath");
		String out = "";
		String[] p = serverpath.split("/");
		for (int i = 0; i < p.length - 1; i++) {
			if (!"".equals(p[i])) {
				out = out + " " + p[i].length() + " " + p[i];
			}
		}
		RemotePathEle.setText(("1 0" + out).replace("  ", " "));
		Element DownloadEle = FileEle.addElement("Download");
		DownloadEle.setText("1");
		Element SizeEle = FileEle.addElement("Size");
		SizeEle.setText(fee.getSize());
		Element DataTypeEle = FileEle.addElement("DataType");
		DataTypeEle.setText("1");
		Element OverwriteActionEle = FileEle.addElement("OverwriteAction");
		OverwriteActionEle.setText("1");
	}

	/**
	 * 生成word
	 * 
	 * @param tempPath
	 *            request.getRealPath("/")+"word\\"
	 * @param tempDocPath
	 * @param fileName
	 * @throws Exception 
	 */
	public void createWord(String tempPath, String tempDocPath, String tempHeadPath, String fileName, BookEntity be) throws Exception {
		String tempDocStr = FileUtil.fileInput(tempDocPath, "utf-8");
		String outDocPath = tempPath + "out\\word\\document.xml";
		String outHeadPath = tempPath + "out\\word\\header1.xml";
		Field[] fields = be.getClass().getFields();
		for (Field f : fields) {
			tempDocStr = tempDocStr.replace("<w:t>" + f.getName() + "</w:t>", ("<w:t>" + repWordChar((f.get(be) + "")) + "</w:t>")).replace("<w:t>null</w:t>", "<w:t></w:t>");
		}
		FileUtil.fileOutput(outDocPath, "utf-8", tempDocStr);
		FileUtil.fileOutput(outHeadPath, "utf-8", FileUtil.fileInput(tempHeadPath, "utf-8").replace("<w:t>book_serial_number</w:t>", "<w:t>" + be.getBook_serial_number() + "</w:t>"));
		deleteExistFile(tempPath + "word\\");
		new File(tempPath + "word").mkdirs();
		FileUtil.zipFiles(tempPath + "word\\" + fileName, tempPath + "out");
	}

	/**
	 * 替换mysql特殊字符
	 * 
	 * @param s
	 * @return
	 */
	public String repMySqlChar(String s) {
		return s.replace("\\", "\\\\\\").replace("_", "\\_").replace("%", "\\%").replace("'", "''");
	}

	/**
	 * 替换word特殊字符
	 * 
	 * @param s
	 * @return
	 */
	public String repWordChar(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	/**
	 * 创建FTP目录
	 * 
	 * @param bookEntity
	 * @return
	 */
	public int createFtpFold(BookEntity bookEntity, String ftpConfigXmlPath) {
		FtpUtil ftp = new FtpUtil();
		boolean flag = ftp.connectFtp(ftpConfigXmlPath);
		if (!flag) { // 登录FTP服务器失败
			return -1;
		}
		String book_neiwen_serverpath = StringUtil.ObjectToString(bookEntity.getBook_neiwen_serverpath()); // 内文
		ftp.createFold(book_neiwen_serverpath);
		String book_cover_serverpath = StringUtil.ObjectToString(bookEntity.getBook_cover_serverpath()); // 封面
		ftp.createFold(book_cover_serverpath);
		String book_font_serverpath = StringUtil.ObjectToString(bookEntity.getBook_font_serverpath()); // 字体
		ftp.createFold(book_font_serverpath);
		String book_pdf_publish_serverpath = StringUtil.ObjectToString(bookEntity.getBook_pdf_publish_serverpath()); // 出版PDF
		ftp.createFold(book_pdf_publish_serverpath);
		String book_word_serverpath = StringUtil.ObjectToString(bookEntity.getBook_word_serverpath()); // WORD
		ftp.createFold(book_word_serverpath);
		String book_xml_serverpath = StringUtil.ObjectToString(bookEntity.getBook_xml_serverpath()); // XML
		ftp.createFold(book_xml_serverpath);
		String book_epub_serverpath = StringUtil.ObjectToString(bookEntity.getBook_epub_serverpath()); // EPUB
		ftp.createFold(book_epub_serverpath);
		String book_mobi_serverpath = StringUtil.ObjectToString(bookEntity.getBook_mobi_serverpath()); // MOBI
		ftp.createFold(book_mobi_serverpath);
		String book_pdf_read_serverpath = StringUtil.ObjectToString(bookEntity.getBook_pdf_read_serverpath()); // 阅读PDF
		ftp.createFold(book_pdf_read_serverpath);
		String book_html_serverpath = StringUtil.ObjectToString(bookEntity.getBook_html_serverpath()); // HTML
		ftp.createFold(book_html_serverpath);
		ftp.ftpLogout();
		return 1;
	}
	
	/**
	 * 补录最初部分数据时创建目录
	 * @param ftpConfigXmlPath
	 * @return
	 */
	public int createFtpFold(String ftpConfigXmlPath) throws Exception{
//		FtpUtil ftp = new FtpUtil();
//		boolean flag = ftp.connectFtp(ftpConfigXmlPath);
//		if (!flag) { // 登录FTP服务器失败
//			return -1;
//		} else {
			List<BookEntity> bookList = bookDao.getBookListByHql("from BookEntity");
			for(BookEntity be : bookList) {
				String bookPath = ConfigInfo.FTP_ROOT+"\\201409之前书目\\"+be.getBook_serial_number();
				createFoldByPath(bookPath+"\\排版");
				createFoldByPath(bookPath+"\\封面");
				createFoldByPath(bookPath+"\\字体");
				createFoldByPath(bookPath+"\\分层PDF");
				createFoldByPath(bookPath+"\\WORD");
				createFoldByPath(bookPath+"\\XML");
				createFoldByPath(bookPath+"\\EPUB");
				createFoldByPath(bookPath+"\\MOBI");
				createFoldByPath(bookPath+"\\阅读PDF");
				createFoldByPath(bookPath+"\\HTML");
				createFoldByPath(bookPath+"\\合同");
			}
//			ftp.ftpLogout();
			return 1;
//		}
	}
	
	private void createFoldByPath(String path) throws Exception {
		File pathFile = new File(path);
		pathFile.mkdirs();
	}

	/**
	 * 创建本地目录，代替FTP目录
	 */
	public void createFold(BookEntity bookEntity) throws Exception{
		String paiban = ConfigInfo.FTP_ROOT + bookEntity.getBook_neiwen_serverpath().replace("/", "\\");
		String cover = ConfigInfo.FTP_ROOT + bookEntity.getBook_cover_serverpath().replace("/", "\\");
		String font = ConfigInfo.FTP_ROOT + bookEntity.getBook_font_serverpath().replace("/", "\\");
		String ppdf = ConfigInfo.FTP_ROOT + bookEntity.getBook_pdf_publish_serverpath().replace("/", "\\");
		String word = ConfigInfo.FTP_ROOT + bookEntity.getBook_word_serverpath().replace("/", "\\");
		String xml = ConfigInfo.FTP_ROOT + bookEntity.getBook_xml_serverpath().replace("/", "\\");
		String epub = ConfigInfo.FTP_ROOT + bookEntity.getBook_epub_serverpath().replace("/", "\\");
		String mobi = ConfigInfo.FTP_ROOT + bookEntity.getBook_mobi_serverpath().replace("/", "\\");
		String rpdf = ConfigInfo.FTP_ROOT + bookEntity.getBook_pdf_read_serverpath().replace("/", "\\");
		String html = ConfigInfo.FTP_ROOT + bookEntity.getBook_html_serverpath().replace("/", "\\");
		String constract = ConfigInfo.FTP_ROOT + bookEntity.getBook_contract_serverpath().replace("/", "\\");
		File paibanFile = new File(paiban);
		paibanFile.mkdirs();
		File coverFile = new File(cover);
		coverFile.mkdirs();
		File fontFile = new File(font);
		fontFile.mkdirs();
		File ppdfFile = new File(ppdf);
		ppdfFile.mkdirs();
		File wordFile = new File(word);
		wordFile.mkdirs();
		File xmlFile = new File(xml);
		xmlFile.mkdirs();
		File epubFile = new File(epub);
		epubFile.mkdirs();
		File mobiFile = new File(mobi);
		mobiFile.mkdirs();
		File rpdfFile = new File(rpdf);
		rpdfFile.mkdirs();
		File htmlFile = new File(html);
		htmlFile.mkdirs();
		File constractFile = new File(constract);
		constractFile.mkdirs();

		new File(cover.replace("MOBI","电子书封面")).mkdirs();
		new File(cover.replace("MOBI", "样章")).mkdirs();

	}

	/**
	 * 根据txt中的book_serial_number删除ftp目录
	 * @param ftpFoldPath
	 * @return
	 */
	public boolean delFtpFold(String ftpFoldPath) throws Exception{
		String content = FileUtil.fileInput2(ftpFoldPath, "utf-8");
		for(String fold : content.split("@@")) {
			if(fold.startsWith("wuzhou")||fold.startsWith("cicc")) {
				String foldName = fold.substring(fold.indexOf("\\"));
				FileUtil.deleteDir(new File(ConfigInfo.FTP_ROOT + "\\201409之前书目"+foldName));
				//log.debug(ConfigInfo.FTP_ROOT + "\\201409之前书目"+foldName);
			} else {
				FileUtil.deleteDir(new File(ConfigInfo.FTP_ROOT + "\\"+fold));
				//log.debug(ConfigInfo.FTP_ROOT + "\\"+fold);
			}
		}
		return true;
	}
	
	/**
	 * 组装资源列表
	 * @param list
	 * @return
	 */
	public List<BookResourceEntity> getResourceList(List<BookEntity> list) {
		if(list==null||list.isEmpty()) return null;
		List<BookResourceEntity> brList = new ArrayList<BookResourceEntity>();
		for(BookEntity be : list) {
			String book_neiwen_serverpath = ConfigInfo.FTP_ROOT+be.getBook_neiwen_serverpath().replace("/", "\\");
			System.out.println(book_neiwen_serverpath);
			double neiwen_size = FileUtil.getDirSize(new File(book_neiwen_serverpath));
			String book_cover_serverpath = ConfigInfo.FTP_ROOT+be.getBook_cover_serverpath().replace("/", "\\");
			double cover_size = FileUtil.getDirSize(new File(book_cover_serverpath));
			String book_font_serverpath = ConfigInfo.FTP_ROOT+be.getBook_font_serverpath().replace("/", "\\");
			double font_size = FileUtil.getDirSize(new File(book_font_serverpath));
			String book_pdf_publish_serverpath = ConfigInfo.FTP_ROOT+be.getBook_pdf_publish_serverpath().replace("/", "\\");
			double pdf_publish_size = FileUtil.getDirSize(new File(book_pdf_publish_serverpath));
			String book_word_serverpath = ConfigInfo.FTP_ROOT+be.getBook_word_serverpath().replace("/", "\\");
			double word_size = FileUtil.getDirSize(new File(book_word_serverpath));
			String book_xml_serverpath = ConfigInfo.FTP_ROOT+be.getBook_xml_serverpath().replace("/", "\\");
			double xml_size = FileUtil.getDirSize(new File(book_xml_serverpath));
			String book_epub_serverpath = ConfigInfo.FTP_ROOT+be.getBook_epub_serverpath().replace("/", "\\");
			double epub_size = FileUtil.getDirSize(new File(book_epub_serverpath));
			String book_mobi_serverpath = ConfigInfo.FTP_ROOT+be.getBook_mobi_serverpath().replace("/", "\\");
			double mobi_size = FileUtil.getDirSize(new File(book_mobi_serverpath));
			String book_pdf_read_serverpath = ConfigInfo.FTP_ROOT+be.getBook_pdf_read_serverpath().replace("/", "\\");
			double pdf_read_size = FileUtil.getDirSize(new File(book_pdf_read_serverpath));
			String book_html_serverpath = ConfigInfo.FTP_ROOT+be.getBook_html_serverpath().replace("/", "\\");
			double html_size = FileUtil.getDirSize(new File(book_html_serverpath));
			String book_contract_serverpath = ConfigInfo.FTP_ROOT+be.getBook_contract_serverpath().replace("/", "\\");
			double contract_size = FileUtil.getDirSize(new File(book_contract_serverpath));
			
			BookResourceEntity bre = new BookResourceEntity();
			bre.setBook_id(be.getBook_id());
			bre.setBook_name_cn(be.getBook_name_cn());
			bre.setBook_language(be.getBook_language());
			bre.setBook_serial_number(be.getBook_serial_number());
			bre.setNeiwen_size(neiwen_size);
			bre.setCover_size(cover_size);
			bre.setFont_size(font_size);
			bre.setPdf_publish_size(pdf_publish_size);
			bre.setWord_size(word_size);
			bre.setXml_size(xml_size);
			bre.setEpub_size(epub_size);
			bre.setMobi_size(mobi_size);
			bre.setPdf_read_size(pdf_read_size);
			bre.setHtml_size(html_size);
			bre.setContract_size(contract_size);
			brList.add(bre);
		}
		return brList;
	}
	
	/**
	 * 统一修改文件夹名
	 * “印刷PDF”=》“分层PDF”
	 * @return
	 */
	public void renamePDFFold() {
		File [] files = new File(ConfigInfo.FTP_ROOT).listFiles();
		for(File file : files) {
			File [] subFiles = file.listFiles();
			for(File subFile : subFiles) {
				File [] subSubFiles = subFile.listFiles();
				for(File subSubFile : subSubFiles) {
					String filePath = subSubFile.getAbsolutePath();
					if(filePath.endsWith("印刷PDF")) {
						log.debug("改名："+filePath);
						subSubFile.renameTo(new File(filePath.replace("印刷PDF", "分层PDF")));
					}
				}
			}
		}
	}

	/**
	 * 更新图书数据的中文
	 * @param excelPath
	 * @return
	 */
	public String updateBookInfo1(String excelPath) throws Exception{
		StringBuffer sb = new StringBuffer();
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(excelPath)));
		XSSFSheet sheet = wb.getSheet("books");
		for(int i = 1; i < sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			String id = getCellValue(row.getCell(0));
			String isbn = getCellValue(row.getCell(1));
			String seriesName = getCellValue(row.getCell(2));
			String bookName = getCellValue(row.getCell(3));
			String bookIntro = getCellValue(row.getCell(4)).replace("'", "’");
			String zhuangzhen = getCellValue(row.getCell(5));
			String bookWeight = getCellValue(row.getCell(6));
			String keyword = getCellValue(row.getCell(7)).replace(";", ",");
			List<BookEntity> list = bookDao.getBookListByHql("from BookEntity where book_isbn = '"+isbn+"'");
			if(list==null||list.isEmpty()) { //未找到
				sb.append("<p class='text-danger'>").append(id).append("\t").append(isbn).append("\t").append(bookName).append("\t未找到</p>");
			} else { //大于1条
				if(list.size()>1) {
					sb.append("<p class='text-warning'>").append(id).append("\t").append(isbn).append("\t").append(bookName).append("\t找到多本书</p>");
				} else {
					BookEntity be = list.get(0);
					be.setBook_series_cn(seriesName);
					be.setBook_name_cn(bookName);
					be.setBook_content_intr_cn(bookIntro);
					be.setBook_zhuangzhen_class(zhuangzhen);
					be.setBook_weight(Float.valueOf(bookWeight));
					be.setBook_keyword_cn(keyword);
					bookDao.updateBook(be);
					sb.append("<p class='text-info'>").append(id).append("\t").append(isbn).append("\t").append(bookName).append("更新成功</p>");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 更新图书数据的啊文
	 * @param excelPath
	 * @return
	 */
	public String updateBookInfo2(String excelPath) throws Exception{
		StringBuffer sb = new StringBuffer();
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(excelPath)));
		XSSFSheet sheet = wb.getSheet("books");
		for(int i = 1; i < sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			String id = getCellValue(row.getCell(0));
			String isbn = getCellValue(row.getCell(1));
			String bookNameCn = getCellValue(row.getCell(2));
			String bookNameE = getCellValue(row.getCell(3));
			String bookAuthorE = getCellValue(row.getCell(4));
			String bookIntroE = getCellValue(row.getCell(5));
			String keyWordE = getCellValue(row.getCell(6));
			String pageCount = getCellValue(row.getCell(7));
			String bookSize = getCellValue(row.getCell(8));
			List<BookEntity> list = bookDao.getBookListByHql("from BookEntity where book_isbn = '"+isbn+"'");
			if(list==null||list.isEmpty()) { //未找到
				sb.append("<p class='text-danger'>").append(id).append("\t").append(isbn).append("\t").append(bookNameCn).append("\t未找到</p>");
			} else { //大于1条
				if(list.size()>1) {
					sb.append("<p class='text-warning'>").append(id).append("\t").append(isbn).append("\t").append(bookNameCn).append("\t找到多本书</p>");
				} else {
					BookEntity be = list.get(0);
					be.setBook_name_cn(bookNameCn);
					be.setBook_name_e(bookNameE);
					be.setBook_author_e(bookAuthorE);
					be.setBook_content_intr_e(bookIntroE);
					be.setBook_keyword_e(keyWordE);
					be.setBook_neiwen_page_count(StringUtil.StringToInt(pageCount));
					be.setBook_size(bookSize);
					bookDao.updateBook(be);
					sb.append("<p class='text-info'>").append(id).append("\t").append(isbn).append("\t").append(bookNameCn).append("更新成功</p>");
				}
			}
		}
		return sb.toString();
	}
	
	private String getCellValue(XSSFCell cell) {
		if (cell == null)
			return "";
		String cellValue = "";
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			DecimalFormat df = new DecimalFormat("0");
			cellValue = df.format(cell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_STRING:
			cellValue = StringUtil.ObjectToString(cell.getStringCellValue());
			break;
		case XSSFCell.CELL_TYPE_FORMULA: //公式
			try{
//				cellValue = cell.getStringCellValue();
				cellValue = String.valueOf(cell.getNumericCellValue());
			} catch(Exception ex){
				cellValue = String.valueOf(cell.getRichStringCellValue());
			}
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			cellValue = "";
			break;
		default:
			cellValue = "";
		}
		return cellValue;
	}

	private String getCellValue2(XSSFCell cell) {
		if (cell == null)
			return "";
		String cellValue = "";
		switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_NUMERIC:
				DecimalFormat df = new DecimalFormat("0.00");
				cellValue = df.format(cell.getNumericCellValue());
				break;
			case XSSFCell.CELL_TYPE_STRING:
				cellValue = StringUtil.ObjectToString(cell.getStringCellValue());
				break;
			case XSSFCell.CELL_TYPE_FORMULA: //公式
				try{
//				cellValue = cell.getStringCellValue();
					cellValue = String.valueOf(cell.getNumericCellValue());
				} catch(Exception ex){
					cellValue = String.valueOf(cell.getRichStringCellValue());
				}
				break;
			case XSSFCell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			default:
				cellValue = "";
		}
		return cellValue;
	}

	public String historyBookArchive() throws Exception{
		StringBuffer sb = new StringBuffer();
		String userXml = "<root><item userId=\"2\" userName=\"jingxm\" nickName=\"荆孝敏\" /><item userId=\"3\" userName=\"zhenglei\" nickName=\"郑磊\" /><item userId=\"4\" userName=\"qiuhy\" nickName=\"邱红艳\" /><item userId=\"5\" userName=\"dongyu\" nickName=\"董宇\" /><item userId=\"6\" userName=\"suiyue\" nickName=\"隋钺\" /><item userId=\"7\" userName=\"gaojj\" nickName=\"高佳佳\" /><item userId=\"8\" userName=\"fuping\" nickName=\"付平\" /><item userId=\"10\" userName=\"gaolei\" nickName=\"高磊\" /><item userId=\"11\" userName=\"suqian\" nickName=\"苏谦\" /><item userId=\"12\" userName=\"zhanghong\" nickName=\"张红\" /><item userId=\"13\" userName=\"wangli\" nickName=\"王莉\" /><item userId=\"14\" userName=\"jiangshan\" nickName=\"姜珊\" /><item userId=\"15\" userName=\"huangjing\" nickName=\"黄静\" /><item userId=\"17\" userName=\"dengjh\" nickName=\"邓锦辉\" /><item userId=\"19\" userName=\"liudan\" nickName=\"刘丹\" /><item userId=\"20\" userName=\"zengxu\" nickName=\"曾旭\" /><item userId=\"21\" userName=\"taoyz\" nickName=\"陶昱铮\" /><item userId=\"22\" userName=\"zhull\" nickName=\"朱莉莉\" /><item userId=\"23\" userName=\"zhangmj\" nickName=\"张美景\" /><item userId=\"24\" userName=\"wangfeng\" nickName=\"王峰\" /><item userId=\"25\" userName=\"qiaofei\" nickName=\"乔飞\" /><item userId=\"26\" userName=\"songby\" nickName=\"宋博雅\" /><item userId=\"27\" userName=\"zhangcy\" nickName=\"张彩芸\" /><item userId=\"28\" userName=\"huangjm\" nickName=\"黄金敏\" /><item userId=\"29\" userName=\"dongzhi\" nickName=\"董智\" /><item userId=\"30\" userName=\"pengting\" nickName=\"彭婷\" /><item userId=\"31\" userName=\"wangss\" nickName=\"王珊珊\" /><item userId=\"32\" userName=\"jiangchao\" nickName=\"姜超\" /><item userId=\"33\" userName=\"liangjj\" nickName=\"梁晶晶\" /><item userId=\"34\" userName=\"xuxs\" nickName=\"徐醒生\" /><item userId=\"35\" userName=\"tanghw\" nickName=\"汤贺伟\" /><item userId=\"36\" userName=\"wuym\" nickName=\"吴娅民\" /><item userId=\"37\" userName=\"liuyang\" nickName=\"刘阳\" /><item userId=\"38\" userName=\"chenhao\" nickName=\"陈浩\" /></root>";
		Document doc = null;
		//有责编的
		String hql = "from BookEntity where book_editor != '' and book_editor !='PM文件打不开' and user_id in (1, 39)";
		List<BookEntity> list = null;
		try{
			doc = DocumentHelper.parseText(userXml);
			list = bookDao.getBookListByHql(hql);
			if(list==null||list.isEmpty()) {
				sb.append("<p class='text-danger'>hql：").append(hql).append("没有查询到图书</p>");
				return "";
			}
			for(BookEntity be : list) {
				String editor = be.getBook_editor();
				int userId = be.getUser_id();
				Element ele = getEditorUserName(editor, doc);
				if(ele!=null) { //
					log.debug(editor+"==");
					String neiwen = be.getBook_neiwen_serverpath();
					String cover = be.getBook_cover_serverpath();
					String font = be.getBook_font_serverpath();
					String pdf_publish = be.getBook_pdf_publish_serverpath();
					String word = be.getBook_word_serverpath();
					String xml = be.getBook_xml_serverpath();
					String epub = be.getBook_epub_serverpath();
					String mobi = be.getBook_mobi_serverpath();
					String pdf_read = be.getBook_pdf_read_serverpath();
					String html = be.getBook_html_serverpath();
					String contract = be.getBook_contract_serverpath();
					
					String srcFold = ConfigInfo.FTP_ROOT.replace("\\", "/")+"/"+neiwen.split("/")[1]+"/"+be.getBook_serial_number();
					
					editor = ele.attributeValue("userName").trim();
					log.debug(editor);
					userId = Integer.parseInt(ele.attributeValue("userId"));
					neiwen = neiwen.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					cover = cover.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					font = font.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					pdf_publish = pdf_publish.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					word = word.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					xml = xml.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					epub = epub.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					mobi = mobi.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					pdf_read = pdf_read.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					html = html.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					contract = contract.replace("201409之前书目", editor).replace("cicc/", editor+"/");
					be.setUser_id(userId);
					be.setBook_neiwen_serverpath(neiwen);
					be.setBook_cover_serverpath(cover);
					be.setBook_font_serverpath(font);
					be.setBook_pdf_publish_serverpath(pdf_publish);
					be.setBook_word_serverpath(word);
					be.setBook_xml_serverpath(xml);
					be.setBook_epub_serverpath(epub);
					be.setBook_mobi_serverpath(mobi);
					be.setBook_pdf_read_serverpath(pdf_read);
					be.setBook_html_serverpath(html);
					be.setBook_contract_serverpath(contract);
					String destFold = ConfigInfo.FTP_ROOT.replace("\\", "/")+"/"+editor+"/"+be.getBook_serial_number();
					File file = new File(srcFold);
					log.debug("文件是否存在："+file.exists());
					if(file.exists()) {
						FileUtil.copyFiles(srcFold, destFold, false);
						sb.append("<p class='text-info'>迁徙成功：").append(be.getBook_serial_number()+"\t"+be.getBook_name_cn()).append("</p>");
//						int flag = bookDao.addBook(be);
//						if(flag==1) {
//							log.debug("数据库更新成功："+be.getBook_serial_number()+"\t"+be.getBook_name_cn());
//							sb.append("<p class='text-info'>更新数据库成功：").append(be.getBook_serial_number()+"\t"+be.getBook_name_cn()).append("</p>");
//						} else {
//							log.error("数据库更新失败："+be.getBook_serial_number()+"\t"+be.getBook_name_cn());
//							sb.append("<p class='text-danger'>更新数据库失败：").append(be.getBook_serial_number()+"\t"+be.getBook_name_cn()).append("</p>");
//						}
					} else {
						createDir(srcFold);
						createDir(destFold);
						//sb.append("<p class='text-danger'>目录不存在：").append(srcFold).append("</p>");
					}
				} else {
					log.debug("责编找不到当作离退休处理");
					//sb.append("<p class='text-warning'>责编找不到当作离退休处理【"+userId+"】：").append(be.getBook_serial_number()+"\t"+be.getBook_name_cn()).append("</p>");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return sb.toString();
	}
	
	/**
	 * 根据用户的昵称，获取用户名
	 * 如：董宇，dongyu
	 * @param editor
	 * @param doc
	 * @return
	 */
	public Element getEditorUserName(String editor, Document doc) {
		editor = editor.replace(" ", "").replace("；", ";");
		if(editor.indexOf(";")!=-1) {
			editor = editor.substring(0, editor.indexOf(";"));
		}
		return (Element)doc.selectSingleNode("//item[@nickName='"+editor+"']");
	}
	
	public void createDir(String bookSerialNumberPath) {
		String dir = bookSerialNumberPath;
		new File(dir+"\\EPUB").mkdirs();
		new File(dir+"\\HTML").mkdirs();
		new File(dir+"\\MOBI").mkdirs();
		new File(dir+"\\WORD").mkdirs();
		new File(dir+"\\XML").mkdirs();
		new File(dir+"\\封面").mkdirs();
		new File(dir+"\\合同").mkdirs();
		new File(dir+"\\排版").mkdirs();
		new File(dir+"\\字体").mkdirs();
		new File(dir+"\\分层PDF").mkdirs();
		new File(dir+"\\阅读PDF").mkdirs();
	}
	
	
	public String createFtpFold2() throws Exception{
		String result="";
		String userXml = "<root><item userId=\"2\" userName=\"jingxm\" nickName=\"荆孝敏\" /><item userId=\"3\" userName=\"zhenglei\" nickName=\"郑磊\" /><item userId=\"4\" userName=\"qiuhy\" nickName=\"邱红艳\" /><item userId=\"5\" userName=\"dongyu\" nickName=\"董宇\" /><item userId=\"6\" userName=\"suiyue\" nickName=\"隋钺\" /><item userId=\"7\" userName=\"gaojj\" nickName=\"高佳佳\" /><item userId=\"8\" userName=\"fuping\" nickName=\"付平\" /><item userId=\"10\" userName=\"gaolei\" nickName=\"高磊\" /><item userId=\"11\" userName=\"suqian\" nickName=\"苏谦\" /><item userId=\"12\" userName=\"zhanghong\" nickName=\"张红\" /><item userId=\"13\" userName=\"wangli\" nickName=\"王莉\" /><item userId=\"14\" userName=\"jiangshan\" nickName=\"姜珊\" /><item userId=\"15\" userName=\"huangjing\" nickName=\"黄静\" /><item userId=\"17\" userName=\"dengjh\" nickName=\"邓锦辉\" /><item userId=\"19\" userName=\"liudan\" nickName=\"刘丹\" /><item userId=\"20\" userName=\"zengxu\" nickName=\"曾旭\" /><item userId=\"21\" userName=\"taoyz\" nickName=\"陶昱铮\" /><item userId=\"22\" userName=\"zhull\" nickName=\"朱莉莉\" /><item userId=\"23\" userName=\"zhangmj\" nickName=\"张美景\" /><item userId=\"24\" userName=\"wangfeng\" nickName=\"王峰\" /><item userId=\"25\" userName=\"qiaofei\" nickName=\"乔飞\" /><item userId=\"26\" userName=\"songby\" nickName=\"宋博雅\" /><item userId=\"27\" userName=\"zhangcy\" nickName=\"张彩芸\" /><item userId=\"28\" userName=\"huangjm\" nickName=\"黄金敏\" /><item userId=\"29\" userName=\"dongzhi\" nickName=\"董智\" /><item userId=\"30\" userName=\"pengting\" nickName=\"彭婷\" /><item userId=\"31\" userName=\"wangss\" nickName=\"王珊珊\" /><item userId=\"32\" userName=\"jiangchao\" nickName=\"姜超\" /><item userId=\"33\" userName=\"liangjj\" nickName=\"梁晶晶\" /><item userId=\"34\" userName=\"xuxs\" nickName=\"徐醒生\" /><item userId=\"35\" userName=\"tanghw\" nickName=\"汤贺伟\" /><item userId=\"36\" userName=\"wuym\" nickName=\"吴娅民\" /><item userId=\"37\" userName=\"liuyang\" nickName=\"刘阳\" /><item userId=\"38\" userName=\"chenhao\" nickName=\"陈浩\" /></root>";
		List<BookEntity> bookList = bookDao.getBookListByHql("from BookEntity where book_del_flag !=1");
		Document doc = DocumentHelper.parseText(userXml);
		for(BookEntity be : bookList) {
			int userId = be.getUser_id();
			String editor = "201409之前书目";
			if(userId==1) {
				editor = "201409之前书目";
			} else if(userId==39) {
				editor = "cicc";
			} else {
				editor = ((Element)doc.selectSingleNode("//item[@userId='"+String.valueOf(userId)+"']")).attributeValue("userName");
			}
			String destFold = ConfigInfo.FTP_ROOT.replace("\\", "/")+"/"+editor+"/"+be.getBook_serial_number();
			File file = new File(destFold);
			if(!file.exists()) {
				createDir(destFold);
				result+="<p class='text-info'>补充目录："+destFold+"</p>";
				log.debug("补充目录："+destFold);
			}
		}
		return result;
	}
	
	
	public void createFtpFold3() throws Exception {
		String path = ConfigInfo.FTP_ROOT;
		File [] files = new File(path).listFiles();
		for(File file : files) {
			File []fs = file.listFiles();
			for(File f : fs) {
				new File(f.getAbsolutePath()+"\\电子书封面").mkdirs();
				new File(f.getAbsolutePath()+"\\样章").mkdirs();
			}
		}
	}

	/**
	 * 加载excel
	 * @param excelPath
	 * @return
	 * @throws Exception
	 */
	public String getSheetsByBookInfoExcel(String excelPath) throws Exception {
		String result="";
		File excelFile = new File(excelPath);
		if(!excelFile.exists()) {
			result = "<p style='color:red'>excel路径有误："+excelPath+"</p>";
		} else {
			try {
				InputStream in = new FileInputStream(excelFile);
				XSSFWorkbook wb = new XSSFWorkbook(in);
				for(int i = 1; i < wb.getNumberOfSheets();i++) {
					XSSFSheet sheet = wb.getSheetAt(i);
					String sheetName = sheet.getSheetName();
					//cusData 存放数据库对应列名
					//cusData 存放sheetId
					result+="<div class='col-sm-4'><button class='btn btn-default' name='excelSheet' cusData=''  cusData2='"+i+"' onclick='showTableColumn(this)'>"+sheetName+"</button></div>";
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
		return result;
	}

	/**
	 * 更新图书信息
	 * @param myXml
	 * @return
	 */
	public String updateBookInfoByExcel(String excelPath, String myXml){
		String out = "";
		Document doc = null;
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(new File(excelPath)));
		} catch (IOException e) {
			e.printStackTrace();
			out += "<p class='text-danger'>解析EXCEL出错："+e.getMessage()+"</p>";
			log.error("解析EXCEL有误："+e.getMessage());
		}
		try {

			doc = DocumentHelper.parseText(myXml);
			List<Element> items = doc.selectNodes("//item");
			for(Element itemEle : items) {
				int sheetId = StringUtil.StringToInt(itemEle.attributeValue("sheetId"));
				String updateColumnName = StringUtil.ObjectToString(itemEle.attributeValue("updateColumnName"));
				try {
					out+=updateBookInfoBySheetId(wb, updateColumnName, sheetId);
				} catch (Exception e) {
					e.printStackTrace();
					out+="<p class='text-danger'>更新错误："+e.getMessage()+"</p>";
					log.error("更新错误："+e.getMessage());
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			out += "<p class='text-danger'>解析XML出错："+e.getMessage()+"</p>";
			log.error("解析XML有误："+e.getMessage()+"\t XML结构："+myXml);
		}
		return out;
	}

	private String updateBookInfoBySheetId(XSSFWorkbook wb, String columnName, int sheetId) throws Exception{
		StringBuffer sb = new StringBuffer();
		XSSFSheet sheet = wb.getSheetAt(sheetId);
		BookEntity bb = new BookEntity();
		String columnType = bb.getClass().getField(columnName).getType().toString();
		for(int i = 1; i < sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			String bookSerialNumber = getCellValue(row.getCell(0));
			if("".equals(bookSerialNumber)) {
				continue;
			}
			String bookName = getCellValue(row.getCell(3));
			Object updateColumnVal = null;
			if("class java.lang.Float".equals(columnType)) {
				updateColumnVal = getCellValue2(row.getCell(4));
			} else {
				updateColumnVal = getCellValue(row.getCell(4));
			}
			if("".equals(StringUtil.ObjectToString(updateColumnVal))) continue;
			List<BookEntity> list = bookDao.getBookListByHql("from BookEntity where book_serial_number like '%"+bookSerialNumber.replace("_", "\\_")+"%' and book_del_flag=0");
			if(list==null||list.isEmpty()) { //未找到
				sb.append("<p class='text-danger'>").append(bookSerialNumber).append("\t").append(bookName).append("\t未找到</p>");
			} else { //大于1条
				if(list.size()>1) {
					sb.append("<p class='text-warning'>").append(bookSerialNumber).append("\t").append(bookName).append("\t找到多本书</p>");
				} else {
					BookEntity be = list.get(0);
					BeanUtils.setProperty(be, columnName, updateColumnVal);
					bookDao.updateBook(be);
					sb.append("<p class='text-info'>").append(bookSerialNumber).append("\t").append(bookName).append("更新成功</p>");
				}
			}
		}
		return sb.toString();
	}

	/**
	 *
	 * @param be
	 * @return
	 * 0 通过检查，可以打印word
	 * 1 内文大小没有通过
	 * 2 。。。
	 * 3 。。。
	 * 4 。。。
	 */
	public String checkFileUpload(BookEntity be) {
		String ftpRootPath = ConfigInfo.FTP_ROOT.replace("\\\\","/");
		String neiwenPath = ftpRootPath+be.getBook_neiwen_serverpath(); //50KB
		long neiwenSize = FileUtils.sizeOfDirectory(new File(neiwenPath));
		log.debug(be.getBook_serial_number()+"\tneiwenSize:" + neiwenSize);
		if(neiwenSize<ConfigInfo.LIMIT_NEIWEN_DIRECTORY_SIZE){
			return "1";
		}
		String coverPath = ftpRootPath+be.getBook_cover_serverpath(); //50KB
		long coverSize = FileUtils.sizeOfDirectory(new File(coverPath));
		log.debug(be.getBook_serial_number()+"	coverSize:" + coverSize);
		if(coverSize<ConfigInfo.LIMIT_COVER_DIRECTORY_SIZE) {
			return "2";
		}
		String fencengPath = ftpRootPath+be.getBook_pdf_publish_serverpath(); //100KB
		long fencengSize = FileUtils.sizeOfDirectory(new File(fencengPath));
		log.debug(be.getBook_serial_number()+"	fencengSize:" + fencengSize);
		if(fencengSize<ConfigInfo.LIMIT_FENCENG_PDF_DIRECTORY_SIZE) {
			return "3";
		}
		String contractPath = ftpRootPath+be.getBook_contract_serverpath(); //1KB
		long contractSize = FileUtils.sizeOfDirectory(new File(contractPath));
		log.debug(be.getBook_serial_number()+"	contractSize:"+contractSize);
		if (contractSize<ConfigInfo.LIMIT_CONTRACT_DIRECTORY_SIZE) {
			return "4";
		}
		return "0";
	}


	public String getBookLanDoughnutChartJson() throws Exception {
//		String sql = "select book_language,count(book_id) from wz_book group by book_language";
		String []color = {"#F7464A|#FF5A5E", "#46BFBD|#5AD3D1", "#FDB45C|#FFC870", "#949FB1|#A8B3C5", "#4D5360|#616774",
							"#16A085|1ABC9C", "#27AE60|#2ECC71", "#2980B9|#3498DB", "#8E44AD|#9B59B6", "#2C3E50|#34495E",
							"#F39C12|#F1C40F", "#D35400|#E67E22", "#C0392B|#E74C3C", "#BDC3C7|#ECF0F1", "#7F8C8D|#95A5A6",
							"#CCCCCC|#DDDDDD"
		};
		List<BookLanDoughnutChartEntity> list = bookDao.getBookLanEntityBySql();
		if(list==null||list.isEmpty()) return "";
		String out = "";
		int count=0;
		for(BookLanDoughnutChartEntity bl : list) {
			out+="{ value:\""+ bl.getBookCount() +"\" ,";
			out+=" color:\""+color[count].split("\\|")[0]+"\" ,";
			out+=" highlight:\""+color[count].split("\\|")[1]+"\" ,";
			out+=" label:\""+bl.getBook_language()+"\" },";
			count++;
			if(count>=15) count=0;
		}
		out = "["+StringUtil.ignoreComma(out)+"]";
		return out;
	}

	public String getBookPublishTimeBarChartJson(String time) throws Exception {
//		String sql = "select book_publish_time, count(book_id) from wz_book where book_publish_time like '"+time+"%'  group by book_publish_time order by book_publish_time";
		List<BookLanDoughnutChartEntity> list = bookDao.getBookBarEntityBySql(time);
		if(list==null||list.isEmpty()) return "";
		String out = "{labels : [\"一月\",\"二月\",\"三月\",\"四月\",\"五月\",\"六月\",\"七月\",\"八月\",\"九月\",\"十月\",\"十一月\",\"十二月\"],";
		out+="datasets : [{fillColor : \"rgba(151,187,205,0.5)\"," +
				"strokeColor : \"rgba(151,187,205,0.8)\"," +
				"highlightFill : \"rgba(151,187,205,0.75)\"," +
				"highlightStroke : \"rgba(151,187,205,1)\"," +
				"data : [";
		String data="";
		int [] months = getCountByMonth(list, time);
		for(int m : months){
			data+=m+",";
		}
		out+=StringUtil.ignoreComma(data)+"]}]}";
		return out;
	}

	private int [] getCountByMonth(List<BookLanDoughnutChartEntity> list, String time) {
		int [] months = {0,0,0,0,0,0,0,0,0,0,0,0};
		for(BookLanDoughnutChartEntity bl : list) {
			String m = bl.getBook_language().replace(time+"-", "");
			if("01".equals(m)){
				months[0]= bl.getBookCount();
			} else if("02".equals(m)){
				months[1]= bl.getBookCount();
			} else if("03".equals(m)){
				months[2]= bl.getBookCount();
			} else if("04".equals(m)){
				months[3]= bl.getBookCount();
			} else if("05".equals(m)){
				months[4]= bl.getBookCount();
			} else if("06".equals(m)){
				months[5]= bl.getBookCount();
			} else if("07".equals(m)){
				months[6]= bl.getBookCount();
			} else if("08".equals(m)){
				months[7]= bl.getBookCount();
			} else if("09".equals(m)){
				months[8]= bl.getBookCount();
			} else if("10".equals(m)){
				months[9]= bl.getBookCount();
			} else if("11".equals(m)){
				months[10]= bl.getBookCount();
			} else if("12".equals(m)){
				months[11]= bl.getBookCount();
			}
		}
		return months;
	}

	public static void main(String [] args) throws Exception {
		BookService bs = new BookService();
		bs.createFtpFold3();
	}

}
