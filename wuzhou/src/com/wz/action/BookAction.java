package com.wz.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.wz.common.ColumnMap;
import com.wz.common.ConfigInfo;
import com.wz.common.PageEntity;
import com.wz.entity.*;
import com.wz.service.BookService;
import com.wz.service.ConfigService;
import com.wz.service.LogService;
import com.wz.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookAction extends ActionSupport {
	public static Logger log = Logger.getLogger("wuzhou");
	private BookService bookService;
	private LogService logService;
	private ConfigService configService;
	private BookEntity bookEntity;
	private String downloadName;
	private String FTPUploadConfig;
	private String FTPDownloadConfig;
	private String wordDownloadName;
	private FileInputStream inputStream;
	private FileInputStream FtpInStream;
	private FileInputStream FtpDownInStream;
	private FileInputStream wordInStream;
	private String showColumn; // 表头
	private String theBookId;
	private String printDocx; //是否打印 docx
	private List<BookEntity> bookEntityList;

	private String addBook;

	public String getPrintDocx() {
		return printDocx;
	}

	public void setPrintDocx(String printDocx) {
		this.printDocx = printDocx;
	}

	public String getAddBook() {
		return addBook;
	}

	public void setAddBook(String addBook) {
		this.addBook = addBook;
	}

	public BookEntity getBookEntity() {
		return bookEntity;
	}

	public void setBookEntity(BookEntity bookEntity) {
		this.bookEntity = bookEntity;
	}

	public String getDownloadName() {
		return downloadName;
	}

	public void setDownloadName(String downloadName) {
		this.downloadName = downloadName;
	}

	public String getFTPUploadConfig() {
		return FTPUploadConfig;
	}

	public void setFTPUploadConfig(String fTPUploadConfig) {
		FTPUploadConfig = fTPUploadConfig;
	}

	public FileInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(FileInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getShowColumn() {
		return showColumn;
	}

	public String getFTPDownloadConfig() {
		return FTPDownloadConfig;
	}

	public void setFTPDownloadConfig(String fTPDownloadConfig) {
		FTPDownloadConfig = fTPDownloadConfig;
	}

	public FileInputStream getFtpDownInStream() {
		return FtpDownInStream;
	}

	public void setFtpDownInStream(FileInputStream ftpDownInStream) {
		FtpDownInStream = ftpDownInStream;
	}

	public FileInputStream getFtpInStream() {
		return FtpInStream;
	}

	public void setFtpInStream(FileInputStream ftpInStream) {
		FtpInStream = ftpInStream;
	}

	public void setShowColumn(String showColumn) {
		this.showColumn = showColumn;
	}

	public List<BookEntity> getBookEntityList() {
		return bookEntityList;
	}

	public void setBookEntityList(List<BookEntity> bookEntityList) {
		this.bookEntityList = bookEntityList;
	}

	public String getWordDownloadName() {
		return wordDownloadName;
	}

	public void setWordDownloadName(String wordDownloadName) {
		this.wordDownloadName = wordDownloadName;
	}

	public FileInputStream getWordInStream() {
		return wordInStream;
	}

	public void setWordInStream(FileInputStream wordInStream) {
		this.wordInStream = wordInStream;
	}

	public String getTheBookId() {
		return theBookId;
	}

	public void setTheBookId(String theBookId) {
		this.theBookId = theBookId;
	}

	public BookAction() {
		bookService = new BookService();
		logService = new LogService();
		configService = new ConfigService();
	}

	/**
	 * 返回添加图书信息页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addBookPage() throws Exception {
		addBook = "OK";
		return Action.SUCCESS;
	}

	/**
	 * 返回图书信息列表页面
	 * 
	 * @throws Exception
	 */
	public String bookList() throws Exception {
		try {
			// 获取用户该显示的字段，如果没有就显示默认的5个字段，否则由用户自己指定
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
			if (userEntity == null) { // session不存在，返回登录页面
				return "noLogin";
			} else {
				showColumn = configService.getConfigColumnXmlByUserId(userEntity.getUser_id());
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			return Action.ERROR;
		}
	}

	/**
	 * 我的图书列表
	 * 
	 * @return
	 */
	public String myBookList() {
		try {
			// 获取用户该显示的字段，如果没有就显示默认的5个字段，否则由用户自己指定
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
			if (userEntity == null) { // session不存在，返回登录页面
				return "noLogin";
			} else {
				showColumn = configService.getConfigColumnXmlByUserId(userEntity.getUser_id());
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			return Action.ERROR;
		}
	}

	/**
	 * 数目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public void getBookList() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		RoleEntity re = (RoleEntity) request.getSession().getAttribute("roleEntity");

		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String hql = "from BookEntity where book_del_flag=0 order by book_id desc";
		List<BookEntity> bookList = bookService.getBookListByHql(hql);
		if (bookList == null) {
			out.print("0");
		} else {
			int rowCount = bookList.size();
			PageEntity pageEntity = new PageEntity();
			pageEntity.setCurrentPage(1);
			pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
			pageEntity.setRowCount(rowCount);
			pageEntity.setPageCount(pageEntity.getPageCount(ConfigInfo.PAGE_ROW_COUNT, rowCount));
			map.put("bookList", bookList);
			map.put("pageEntity", pageEntity);
			JSONObject json = JSONObject.fromObject(map);// 将map对象转换成json类型数据
			out.print(json.toString());
		}
		out.flush();
		out.close();
	}

	/**
	 * 根据条件检索
	 * 
	 * @throws Exception
	 */
	public void getBookListByParm() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String searchType = request.getParameter("searchType");
		String searchContent = request.getParameter("searchContent");
		String columnName = ColumnMap.getBookTableColumnNameByCn(searchType);
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String hql = "from BookEntity where " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc";
		List<BookEntity> bookList = bookService.getBookListByHql(hql);
		if (bookList == null) {
			out.print("0");
		} else {
			int rowCount = bookList.size();
			PageEntity pageEntity = new PageEntity();
			pageEntity.setCurrentPage(1);
			pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
			pageEntity.setRowCount(rowCount);
			pageEntity.setPageCount(pageEntity.getPageCount(ConfigInfo.PAGE_ROW_COUNT, rowCount));
			map.put("bookList", bookList);
			map.put("pageEntity", pageEntity);
			JSONObject json = JSONObject.fromObject(map);// 将map对象转换成json类型数据
			out.print(json.toString());
		}
		out.flush();
		out.close();
	}

	/**
	 * 创建“本社”新书
	 * 
	 * @return
	 * @throws Exception
	 */
	public void addBook() throws Exception {
		try {
			LogEntity logEntity = new LogEntity();
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
			if (userEntity == null) {
				throw new Exception();
			} else {
				int flag = 0;
				//设为当前用户
				//bookEntity.setUser_id(userEntity.getUser_id());
				flag = bookService.addBook(bookEntity);
				if (flag == 1) {
					logEntity.setModify_time(new Date());
					logEntity.setModify_type("新书保存");
					logEntity.setBook_serial_number(bookEntity.getBook_serial_number());
					logEntity.setUser_name(userEntity.getNick_name());
					logService.addLog(logEntity);
					// 修改本地创建目录
					bookService.createFold(bookEntity);
					/**
					 * String ftpConfigXmlPath =
					 * request.getRealPath("/")+"xml\\FTPConfig.xml";
					 * //FTP服务器端创建目录 int success =
					 * bookService.createFtpFold(bookEntity, ftpConfigXmlPath);
					 * if(success!=1) { return "ftpError"; }
					 */
					System.out.println("bookId='" + flag + "'");
					out.write("<script type=\"text/javascript\">alert('保存成功!');</script>");
				} else {
					out.write("<script type=\"text/javascript\">alert('保存失败!');</script>");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 创建“其他”新书
	 */
	public void addOtherBook() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
		if (userEntity == null) { // session不存在，返回登录页面
			out.write("-1");
		} else {
			String bsn = StringUtil.ObjectToString(request.getParameter("bsn"));
			if ("".equals(bsn)) { // 未获取到bsn
				out.write("0");
			} else {
				int flag = bookService.addBookByBsn(bsn, userEntity.getUser_id());
				if (flag == 1) { // 添加成功
					LogEntity logEntity = new LogEntity();
					logEntity.setModify_time(new Date());
					logEntity.setModify_type("创建");
					logEntity.setBook_serial_number(bsn);
					logEntity.setUser_name(userEntity.getNick_name());
					logService.addLog(logEntity);
					out.write("1");
				} else {
					out.write("2");
				}
			}
		}
		out.flush();
		out.close();
	}

	/**
	 * 删除图书
	 * 
	 * @throws Exception
	 */
	public void deleteBook() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		int bookId = StringUtil.StringToInt(request.getParameter("bookId"));
		if (bookId != 0) {
			int flag = bookService.deleteBook(bookId);
			if (flag == 1) { // 删除成功
				out.print("1");
			} else { // 删除失败
				out.print("2");
			}
		} else {// 未获取到图书ID
			out.print("0");
		}
		out.flush();
		out.close();
	}

	/**
	 * 修改当前图书
	 * 
	 * @return
	 * @throws Exception
	 */
	public String detailBook() throws Exception {
		// HttpServletRequest request = ServletActionContext.getRequest();
		// int bookId = StringUtil.StringToInt(request.getParameter("bookId"));
		// if(bookId==0) {
		// return "noBookId";
		// } else {
		// bookEntity = bookService.getBookEntityById(bookId);
		System.out.println("+++" + theBookId);
		if (null == theBookId || "".equals(theBookId) || "0".equals(theBookId)) {
			return Action.ERROR;
		} else {
			return Action.SUCCESS;
		}
		// }
	}

	/**
	 * 修改当前图书
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editBook() throws Exception {
		// HttpServletRequest request = ServletActionContext.getRequest();
		// int bookId = StringUtil.StringToInt(request.getParameter("bookId"));
		// if(bookId==0) {
		// return "noBookId";
		// } else {
		// bookEntity = bookService.getBookEntityById(bookId);
		if (null == theBookId || "".equals(theBookId) || "0".equals(theBookId)) {
			return Action.ERROR;
		} else {
			return Action.SUCCESS;
		}
		// }
	}

	/**
	 * 保存图书
	 * 
	 * @return
	 * @throws Exception
	 */
	public void updateBook() throws Exception {
		try {
			LogEntity logEntity = new LogEntity();
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
			if (userEntity == null) {
				throw new Exception();
			} else {
				int flag = bookService.updateBook(bookEntity);
				if (flag == 1) {
					logEntity.setModify_time(new Date());
					logEntity.setModify_type("修改");
					logEntity.setBook_serial_number(bookEntity.getBook_serial_number());
					logEntity.setUser_name(userEntity.getNick_name());
					logService.addLog(logEntity);
					// 修改本地创建目录
					bookService.createFold(bookEntity);
					theBookId = bookEntity.getBook_id() + "";
					
					String print = StringUtil.ObjectToString(printDocx);
					if("print".equals(print)) {
						out.write("<script type=\"text/javascript\">parent.createDocxByBookId('"+theBookId+"');</script>");
						printDocx="";
					} else {
						out.write("<script type=\"text/javascript\">alert('保存成功!');</script>");
					}
					/*
					 * String ftpConfigXmlPath =
					 * request.getRealPath("/")+"xml\\FTPConfig.xml";
					 * //FTP服务器端创建目录 int success =
					 * bookService.createFtpFold(bookEntity, ftpConfigXmlPath);
					 * 
					 * if(success!=1) { return "ftpError"; }
					 */
				} else {
					out.write("<script type=\"text/javascript\">alert('保存失败!');</script>");
				}
			}
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * 根据书号获得图书信息
	 */
	public void getBookEntityByBookId() throws Exception {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			int bookId = StringUtil.StringToInt(request.getParameter("bookId"));
			if (bookId == 0) {
				out.print("0");
			} else {
				bookEntity = bookService.getBookEntityById(bookId);
				JSONObject jsonObj = JSONObject.fromObject(bookEntity);
				out.print(jsonObj.toString());
			}
			out.flush();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * JSON转EXCEL
	 * 
	 * @throws Exception
	 */
	public void jsonToExcel() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String jsonString = request.getParameter("jsonString");
		Object obj = request.getSession().getAttribute("userEntity");
		String exportColumn = ConfigInfo.DEFAULT_EXPORT_COLUMN;
		if (obj != null) {
			int userId = ((UserEntity) obj).getUser_id();
			exportColumn = configService.getExportColumnByUserId(userId);
		}
		// System.out.println("jsonString:"+jsonString);
		try {
			String fileName = StringUtil.dateToString("yyyyMMddhhmmss") + ".xlsx";
			bookService.json2Excel(jsonString, request.getRealPath("/") + "excel\\", fileName, exportColumn);
			out.write(fileName);
		} catch (Exception ex) {
			out.write("0");
		}
		out.flush();
		out.close();
	}

	/**
	 * EXCEL下载
	 * 
	 * @return
	 * @throws Exception
	 */
	public String excelDownload() throws Exception {
		if (inputStream != null) {
			inputStream.close();
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		downloadName = request.getParameter("fileName");
		inputStream = new FileInputStream(new File(request.getRealPath("/") + "excel\\" + downloadName));
		return SUCCESS;
	}

	/**
	 * 查看版权到期的图书
	 * 
	 * @return
	 */
	public String copyrightExpires() throws Exception {
		String hql = "from BookEntity where book_flag=0 and TO_DAYS(book_copyright_expires)-TO_DAYS(now()) <= " + ConfigInfo.COPYRIGHT_EXPIRES_REMIND_DAY + " order by datediff(now(), book_copyright_expires) desc";
		bookEntityList = bookService.getBookListByHql(hql);
		// System.out.println("版权到期图书数量："+bookEntityList.size());
		return Action.SUCCESS;
	}

	/**
	 * 获取是否标记版权过期
	 */
	public void getBookByBiaoji() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		int page = StringUtil.StringToInt(request.getParameter("currentPage"));
		int biaoji = StringUtil.StringToInt(request.getParameter("biaoji"));
		if (page == 0) {
			page = 1;
		}
		String hql = "";
		String countHql = "";
		if (biaoji == 0) { // 未标记
			hql = "from BookEntity where book_flag=0 and book_del_flag=0 and TO_DAYS(book_copyright_expires)-TO_DAYS(now()) <= " + ConfigInfo.COPYRIGHT_EXPIRES_REMIND_DAY + " order by TO_DAYS(book_copyright_expires)-TO_DAYS(now()) desc";
			countHql = "select count(book_id) from wz_book where book_flag =0 and book_del_flag=0 and TO_DAYS(book_copyright_expires)-TO_DAYS(now()) <= " + ConfigInfo.COPYRIGHT_EXPIRES_REMIND_DAY;
		} else if (biaoji == 1) {// 已标记
			hql = "from BookEntity where book_flag = 1 and book_del_flag=0 order by book_copyright_expires desc";
			countHql = "select count(book_id) from wz_book where book_flag = 1 and book_del_flag=0";
		} else {
			out.print("-1");
			return;
		}
		List<BookEntity> bookList = bookService.findPageFromBook(page, ConfigInfo.PAGE_ROW_COUNT, hql);
		int pageCount = bookService.getPageCount(countHql, ConfigInfo.PAGE_ROW_COUNT);
		if (bookList == null) {
			out.print("0");
		} else {
			// int rowCount = bookList.size();
			PageEntity pageEntity = new PageEntity();
			pageEntity.setCurrentPage(1);
			pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
			pageEntity.setRowCount(0);
			pageEntity.setPageCount(pageCount);
			map.put("bookList", bookList);
			map.put("pageEntity", pageEntity);
			JSONObject json = JSONObject.fromObject(map);// 将map对象转换成json类型数据
			out.print(json.toString());
		}
		out.flush();
		out.close();
	}

	/**
	 * 处理版权到期图书，
	 */
	public void handleCopyrightExpires() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		int bookId = StringUtil.StringToInt(request.getParameter("bookId"));
		int flag = bookService.handleCopyrightExpires(bookId); // 版权过期的设置为1，没有过期的设置为0；
		// System.out.println("--"+flag+"--");
		out.print(flag);
		out.close();
	}

	/**
	 * 显示删除的图书
	 * 
	 * @return
	 */
	public String showDelBook() throws Exception {
		// 获取用户该显示的字段，如果没有就显示默认的5个字段，否则由用户自己指定
		HttpServletRequest request = ServletActionContext.getRequest();
		UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
		if (userEntity == null) { // session不存在，返回登录页面
			return "noLogin";
		} else {
			showColumn = configService.getConfigColumnXmlByUserId(userEntity.getUser_id());
			// System.out.println(showColumn);
		}
		return Action.SUCCESS;
	}

	/**
	 * 获取删除图书
	 * 
	 * @throws Exception
	 */
	public void getDelBookList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String hql = "from BookEntity where book_del_flag=1 order by book_id desc";
		List<BookEntity> bookDelList = bookService.getBookListByHql(hql);
		if (bookDelList == null) {
			out.print("0");
		} else {
			int rowCount = bookDelList.size();
			PageEntity pageEntity = new PageEntity();
			pageEntity.setCurrentPage(1);
			pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
			pageEntity.setRowCount(rowCount);
			pageEntity.setPageCount(pageEntity.getPageCount(ConfigInfo.PAGE_ROW_COUNT, rowCount));
			map.put("delBookList", bookDelList);
			map.put("pageEntity", pageEntity);
			JSONObject json = JSONObject.fromObject(map);// 将map对象转换成json类型数据
			out.print(json.toString());
		}
		out.flush();
		out.close();
	}

	/**
	 * 恢复已删除的图书
	 */
	public void recoveryDelBook() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		int bookId = StringUtil.StringToInt(request.getParameter("bookId"));
		int flag = bookService.recoveryDelBook(bookId);
		if (flag == 1) {// 修改成功
			out.write("1");
		} else {// 修改失败
			out.write("0");
		}
		out.close();
	}

	/**
	 * 真实删除图书
	 * 
	 * @return
	 */
	public void realDelBook() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		int bookId = StringUtil.StringToInt(request.getParameter("bookId"));
		int flag = bookService.realDelBook(bookId);
		if (flag == 1) {// 删除成功
			out.write("1");
		} else {// 删除失败
			out.write("0");
		}
		out.close();
	}

	/**
	 * 添加引用
	 */
	public void refByISBN() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		int bookType = StringUtil.StringToInt(request.getParameter("bookType"));// 图书类型，纸书1，电子书2
		String bookIsbn = StringUtil.ObjectToString(request.getParameter("bookIsbn"));
		// System.out.println("图书引用，获取参数：bookType="+bookType+"--book_serial_number="+book_serial_number);
		if (bookType != 0 && !"".equals(bookIsbn)) { //
			int findBookType = 0;
			if (bookType == 1) {
				findBookType = 2;
			} else {
				findBookType = 1;
			}
			BookEntity be = bookService.getBookEntityByISBN(bookIsbn, findBookType);
			if (be == null) { // 未找到相关的图书信息
				out.print("0");
			} else {
				JSONObject json = JSONObject.fromObject(be);
				out.print(json.toString());
			}
		} else { // 获取图书类型和图书编号失败
			out.print("-1");
		}
		out.close();
	}

	/**
	 * 根据路径生成FTP上传的xml
	 */
	public void getTaskXml() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		String booktype = StringUtil.ObjectToString(request.getParameter("booktype"));
		// 纸质书
		String neiwenserver = StringUtil.ObjectToString(request.getParameter("neiwenserver"));
		String neiwenlocal = StringUtil.ObjectToString(request.getParameter("neiwenlocal"));
		String coverserver = StringUtil.ObjectToString(request.getParameter("coverserver"));
		String coverlocal = StringUtil.ObjectToString(request.getParameter("coverlocal"));
		String fontserver = StringUtil.ObjectToString(request.getParameter("fontserver"));
		String fontlocal = StringUtil.ObjectToString(request.getParameter("fontlocal"));
		String pdfpublishserver = StringUtil.ObjectToString(request.getParameter("pdfpublishserver"));
		String pdfpublishlocal = StringUtil.ObjectToString(request.getParameter("pdfpublishlocal"));
		String wordserver = StringUtil.ObjectToString(request.getParameter("wordserver"));
		String wordlocal = StringUtil.ObjectToString(request.getParameter("wordlocal"));
		// 电子书
		String xmlserver = StringUtil.ObjectToString(request.getParameter("xmlserver"));
		String xmllocal = StringUtil.ObjectToString(request.getParameter("xmllocal"));
		String epubserver = StringUtil.ObjectToString(request.getParameter("epubserver"));
		String epublocal = StringUtil.ObjectToString(request.getParameter("epublocal"));
		String mobiserver = StringUtil.ObjectToString(request.getParameter("mobiserver"));
		String mobilocal = StringUtil.ObjectToString(request.getParameter("mobilocal"));
		String pdfreadserver = StringUtil.ObjectToString(request.getParameter("pdfreadserver"));
		String pdfreadlocal = StringUtil.ObjectToString(request.getParameter("pdfreadlocal"));
		String htmlserver = StringUtil.ObjectToString(request.getParameter("htmlserver"));
		String htmllocal = StringUtil.ObjectToString(request.getParameter("htmllocal"));
		String xmlPath = request.getRealPath("/") + "xml\\";
		String xmlName = "FTPUpload" + (new Date().getTime()) + ".xml";
		// System.out.println(xmlPath);
		bookService.createUploadXml(xmlPath, booktype, xmlName, neiwenserver, neiwenlocal, coverserver, coverlocal, fontserver, fontlocal, pdfpublishserver, pdfpublishlocal, wordserver, wordlocal, xmlserver, xmllocal, epubserver, epublocal, mobiserver, mobilocal, pdfreadserver, pdfreadlocal,
				htmlserver, htmllocal);
		// System.out.println(xmlPath+xmlName);
		out.write(xmlName);
		out.close();
	}

	/**
	 * 下载任务列表
	 */
	public String downTaskXml() throws Exception {
		if (FtpInStream != null) {
			FtpInStream.close();
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		FTPUploadConfig = request.getParameter("ftpFileName");
		FtpInStream = new FileInputStream(new File(request.getRealPath("/") + "xml\\" + FTPUploadConfig));
		return SUCCESS;
	}

	/**
	 * 组装ftp服务器上的文件列表，根据目录，遍历文件并生成xml
	 */
	public void getFtpFiles() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		String xmlPath = request.getRealPath("/") + "xml\\";
		String xmlName = "FTPDownload" + (new Date().getTime()) + ".xml";
		// String json =
		// StringUtil.ObjectToString(request.getParameter("jsonString"));
		int userId = StringUtil.StringToInt(request.getParameter("userId"));
		String searchType = StringUtil.ObjectToString(request.getParameter("searchType"));
		String searchContent = StringUtil.ObjectToString(request.getParameter("searchContent"));
		String localpath = StringUtil.ObjectToString(request.getParameter("localpath"));
		String exportXmlCol = StringUtil.ObjectToString(request.getParameter("exportXmlCol"));
		if ("".equals(exportXmlCol) || "".equals(localpath)) {
			out.write("-1");
		} else {
			String hql = "";
			if ("".equals(searchType) || "".equals(searchContent)) {
				if (userId == 0) {
					hql = "from BookEntity where book_del_flag=0 order by book_id desc";
				} else {
					hql = "from BookEntity where user_id = " + userId + " and book_del_flag=0 order by book_id desc";
				}
			} else {
				// mysql 转义
				searchContent = bookService.repMySqlChar(searchContent);
				String speChar = searchContent.contains("{") ? "#}" : ""; // mysql
				String columnName = ColumnMap.getBookTableColumnNameByCn(searchType);
				if("book_publish_time".equals(columnName)) { //出版时间
					if(searchContent.contains(" 到 ")) { //有起始日期，也有结束日期
						if (userId == 0) {
							hql = "from BookEntity where book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc ";
						} else {
							hql = "from BookEntity where user_id = " + userId + " and book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc ";
						}
					} else { //只有起始日期
						if (userId == 0) {
							hql = "from BookEntity where book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc ";
						} else {
							hql = "from BookEntity where user_id = " + userId + " and book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc ";
						}
					}
				} else {
					if (userId == 0) {
						hql = "from BookEntity where " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
					} else {
						hql = "from BookEntity where user_id = " + userId + " and " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
					}
				}
			}
			List<BookEntity> list = bookService.getBookListByHql(hql);
			System.out.println(list.size());
			bookService.createFtpXml(list, exportXmlCol, xmlPath, xmlName, localpath);
			out.write(xmlName);
		}
		out.close();
	}

	/**
	 * 下载生成的xml
	 * 
	 * @return
	 */
	public String downFtpFileXml() throws Exception {
		if (FtpDownInStream != null) {
			FtpDownInStream.close();
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		FTPDownloadConfig = request.getParameter("ftpFileName");
		FtpDownInStream = new FileInputStream(new File(request.getRealPath("/") + "xml\\" + FTPDownloadConfig));
		return SUCCESS;
	}

	/**
	 * 图书列表控制
	 * 
	 * @return
	 */
	public String bookListControl() throws Exception {
		// 获取用户该显示的字段，如果没有就显示默认的5个字段，否则由用户自己指定
		HttpServletRequest request = ServletActionContext.getRequest();
		UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
		if (userEntity == null) { // session不存在，返回登录页面
			return "noLogin";
		} else {
			showColumn = configService.getConfigColumnXmlByUserId(userEntity.getUser_id());
			// System.out.println(showColumn);
		}
		return Action.SUCCESS;
	}

	// ///////////////////////////////////test
	public String allBookList() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
		if (userEntity == null) { // session不存在，返回登录页面
			return "noLogin";
		} else {
			showColumn = configService.getConfigColumnXmlByUserId(userEntity.getUser_id());
		}
		return Action.SUCCESS;
	}

	/**
	 * 根据条件查询图书列表信息
	 */
	public void getBookListByCondition() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		int pageNum = StringUtil.StringToInt(request.getParameter("page"));
		String searchType = StringUtil.ObjectToString(request.getParameter("searchType"));
		String searchContent = StringUtil.ObjectToString(request.getParameter("searchContent"));
		int userId = StringUtil.StringToInt(request.getParameter("userId"));
		String hql = "";
		String countHql = "";
		if (pageNum == 0) {
			pageNum = 1;
		}
		if ("".equals(searchType) || "".equals(searchContent)) {
			if (userId == 0) {
				hql = "from BookEntity where book_del_flag=0 order by book_id desc";
				countHql = "select count(book_id) from wz_book where book_del_flag=0 order by book_id desc";
			} else {
				hql = "from BookEntity where user_id = " + userId + " and book_del_flag=0 order by book_id desc";
				countHql = "select count(book_id) from wz_book where user_id = " + userId + " and book_del_flag=0 order by book_id desc";
			}
		} else {
			// mysql 转义
			searchContent = bookService.repMySqlChar(searchContent);
			String speChar = searchContent.contains("{") ? "#}" : ""; // mysql
			String columnName = ColumnMap.getBookTableColumnNameByCn(searchType);
			
			if("book_publish_time".equals(columnName)) { //出版时间
				if(searchContent.contains(" 到 ")) { //有起始日期，也有结束日期
					if (userId == 0) {
						hql = "from BookEntity where book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc";
					} else {
						hql = "from BookEntity where user_id = " + userId + " and book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where user_id = " + userId + " and book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc";
					}
				} else { //只有起始日期
					if (userId == 0) {
						hql = "from BookEntity where book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc";
					} else {
						hql = "from BookEntity where user_id = " + userId + " and book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where user_id = " + userId + " and book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc";
					}
				}
			} else {
				if (userId == 0) {
					hql = "from BookEntity where " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
					countHql = "select count(book_id) from wz_book where " + columnName + " like '%" + searchContent.trim() + "%' " + speChar + " and book_del_flag = 0 order by book_id desc";
				} else {
					hql = "from BookEntity where user_id = " + userId + " and " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
					countHql = "select count(book_id) from wz_book where user_id = " + userId + " and " + columnName + " like '%" + searchContent.trim() + "%' " + speChar + " and book_del_flag = 0 order by book_id desc";
				}
			}
		}
		System.out.println(hql);
		List<BookEntity> bookList = bookService.findPageFromBook(pageNum, ConfigInfo.PAGE_ROW_COUNT, hql);
		int pageCount = bookService.getPageCount(countHql, ConfigInfo.PAGE_ROW_COUNT);
		PageEntity pageEntity = new PageEntity();
		pageEntity.setCurrentPage(pageNum);
		pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
		pageEntity.setRowCount(0);
		pageEntity.setPageCount(pageCount);
		map.put("bookList", bookList);
		map.put("pageEntity", pageEntity);
		JSONObject json = JSONObject.fromObject(map);// 将map对象转换成json类型数据
		out.print(json.toString());
		out.flush();
		out.close();
	}

	/**
	 * 生成excel
	 */
	public void createExcel() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		String searchType = StringUtil.ObjectToString(request.getParameter("searchType"));
		String searchContent = StringUtil.ObjectToString(request.getParameter("searchContent"));
		int userId = StringUtil.StringToInt(request.getParameter("userId"));
		String hql = "";
		if ("".equals(searchType) || "".equals(searchContent)) {
			if (userId == 0) {
				hql = "from BookEntity where book_del_flag=0 order by book_id desc";
			} else {
				hql = "from BookEntity where user_id = " + userId + " and book_del_flag=0 order by book_id desc";
			}
		} else {
			searchContent = bookService.repMySqlChar(searchContent);
			String columnName = ColumnMap.getBookTableColumnNameByCn(searchType);
//			if (userId == 0) {
//				hql = "from BookEntity where " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
//			} else {
//				hql = "from BookEntity where user_id = " + userId + " and " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
//			}
			if("book_publish_time".equals(columnName)) { //出版时间
				if(searchContent.contains(" 到 ")) { //有起始日期，也有结束日期
					if (userId == 0) {
						hql = "from BookEntity where book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc ";
					} else {
						hql = "from BookEntity where user_id = " + userId + " and book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc ";
					}
				} else { //只有起始日期
					if (userId == 0) {
						hql = "from BookEntity where book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc ";
					} else {
						hql = "from BookEntity where user_id = " + userId + " and book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc ";
					}
				}
			} else {
				if (userId == 0) {
					hql = "from BookEntity where " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
				} else {
					hql = "from BookEntity where user_id = " + userId + " and " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
				}
			}
		}
		System.out.println(hql);
		Object obj = request.getSession().getAttribute("userEntity");
		String exportColumn = ConfigInfo.DEFAULT_EXPORT_COLUMN;
		if (obj != null) {
			int uId = ((UserEntity) obj).getUser_id();
			exportColumn = configService.getExportColumnByUserId(uId);
		}
		List<BookEntity> bookList = bookService.getBookListByHql(hql);
		String fileName = StringUtil.dateToString("yyyyMMddhhmmss") + ".xlsx";
		try {
			bookService.createExcel(request.getRealPath("/") + "excel\\", fileName, bookList, exportColumn);
			out.write(fileName);
		} catch (Exception e) {
			e.printStackTrace();
			out.write("0");
		}
		out.flush();
		out.close();
	}

	/**
	 * 根据图书id，生成word
	 */
	public void createDocxByBookId() throws Exception {
		try {

			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = null;
			out = response.getWriter();
			int bookId = StringUtil.StringToInt(request.getParameter("bookId"));
			if (bookId == 0) { // 未获取到图书编号
				out.write("-1");
				return;
			}
			BookEntity be = bookService.getBookEntityById(bookId);
			if (be == null) { // 未获取到图书信息
				out.write("0");
				return;
			}
			String tempPath = request.getRealPath("/") + "word\\";
			String tempDocPath = tempPath + "temp\\word\\document.xml";
			String tempHeadPath = tempPath + "temp\\word\\header1.xml";
			String fileName = StringUtil.dateToString("yyyyMMddhhmmss") + ".docx";
			try {
				bookService.createWord(tempPath, tempDocPath, tempHeadPath, fileName, be);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.write(fileName);
			out.flush();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	// word下载
	public String downloadWord() throws IOException {
		if (wordInStream != null) {
			wordInStream.close();
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		wordDownloadName = request.getParameter("wordDownloadName");
		wordInStream = new FileInputStream(new File(request.getRealPath("/") + "word\\word\\" + wordDownloadName));
		return SUCCESS;
	}

	/**
	 * ftp服务端创建目录
	 */
	public void createFtpFold() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String ftpConfigXmlPath = request.getRealPath("/") + "xml\\FTPConfig.xml";
		int flag = bookService.createFtpFold(ftpConfigXmlPath);
		if (flag == 1) { // ftp目录创建成功
			out.write("1");
		} else { // ftp目录创建失败
			out.write("0");
		}
		out.close();
	}
	
	/**
	 * 
	 * 根据txt中的book_id删除ftp目录
	 * @throws Exception
	 */
	public void deleteFtpFold() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String ftpFoldPath = request.getRealPath("/") + "tempPage\\delBookFold.txt";
		log.debug("删除FTP目录，FTP文档路径："+ftpFoldPath);
		boolean flag = bookService.delFtpFold(ftpFoldPath);
		if (flag) { // ftp目录删除成功
			out.write("1");
		} else { // ftp目录删除失败
			out.write("0");
		}
		out.close();
	}
	
	/**
	 * 用来查看图书是否上传资源
	 * @return
	 * @throws Exception 
	 */
	public String allResource() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
		if (userEntity == null) { // session不存在，返回登录页面
			return "noLogin";
		} else {
			showColumn = configService.getConfigColumnXmlByUserId(userEntity.getUser_id());
		}
		return Action.SUCCESS;
	}
	
	/**
	 * 分页，上传资源列表
	 * 、、、、、
	 * @throws Exception
	 */
	public void getAllResourceByCondition() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		int pageNum = StringUtil.StringToInt(request.getParameter("page"));
		String searchType = StringUtil.ObjectToString(request.getParameter("searchType"));
		String searchContent = StringUtil.ObjectToString(request.getParameter("searchContent"));
		int userId = StringUtil.StringToInt(request.getParameter("userId"));
		String hql = "";
		String countHql = "";
		if (pageNum == 0) {
			pageNum = 1;
		}
		if ("".equals(searchType) || "".equals(searchContent)) {
			if (userId == 0) {
				hql = "from BookEntity where book_del_flag=0 order by book_id desc";
				countHql = "select count(book_id) from wz_book where book_del_flag=0 order by book_id desc";
			} else {
				hql = "from BookEntity where user_id = " + userId + " and book_del_flag=0 order by book_id desc";
				countHql = "select count(book_id) from wz_book where user_id = " + userId + " and book_del_flag=0 order by book_id desc";
			}
		} else {
			// mysql 转义
			searchContent = bookService.repMySqlChar(searchContent);
			String speChar = searchContent.contains("{") ? "#}" : ""; // mysql
			String columnName = ColumnMap.getBookTableColumnNameByCn(searchType);
			
			if("book_publish_time".equals(columnName)) { //出版时间
				if(searchContent.contains(" 到 ")) { //有起始日期，也有结束日期
					if (userId == 0) {
						hql = "from BookEntity where book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc";
					} else {
						hql = "from BookEntity where user_id = " + userId + " and book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where user_id = " + userId + " and book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc";
					}
				} else { //只有起始日期
					if (userId == 0) {
						hql = "from BookEntity where book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc";
					} else {
						hql = "from BookEntity where user_id = " + userId + " and book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where user_id = " + userId + " and book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc";
					}
				}
			} else {
				if (userId == 0) {
					hql = "from BookEntity where " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
					countHql = "select count(book_id) from wz_book where " + columnName + " like '%" + searchContent.trim() + "%' " + speChar + " and book_del_flag = 0 order by book_id desc";
				} else {
					hql = "from BookEntity where user_id = " + userId + " and " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
					countHql = "select count(book_id) from wz_book where user_id = " + userId + " and " + columnName + " like '%" + searchContent.trim() + "%' " + speChar + " and book_del_flag = 0 order by book_id desc";
				}
			}
		}
		List<BookEntity> bookList = bookService.findPageFromBook(pageNum, ConfigInfo.PAGE_ROW_COUNT, hql);
		List<BookResourceEntity> brList = bookService.getResourceList(bookList);
		int pageCount = bookService.getPageCount(countHql, ConfigInfo.PAGE_ROW_COUNT);
		PageEntity pageEntity = new PageEntity();
		pageEntity.setCurrentPage(pageNum);
		pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
		pageEntity.setRowCount(0);
		pageEntity.setPageCount(pageCount);
		map.put("brList", brList);
		map.put("pageEntity", pageEntity);
		JSONObject json = JSONObject.fromObject(map);// 将map对象转换成json类型数据
		out.print(json.toString());
		out.flush();
		out.close();
	}
	
	/**
	 * 统一修改文件夹名
	 * “印刷PDF”=》“分层PDF”
	 * @return
	 */
	public void renamePDFFold() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bookService.renamePDFFold();
		out.write("1");
		out.close();
	}
	
	/**
	 * 更新图书数据的中文
	 */
	public void updateBookInfo1() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String excelPath = request.getRealPath("/") + "tempPage\\中文导入表格.xlsx";
			String result = bookService.updateBookInfo1(excelPath);
			out.write(result);
		} catch (Exception e) {
			e.printStackTrace();
			out.write("<p class='text-danger'>对不起，更新失败，失败原因："+e.getMessage()+"</p>");
		}
		out.close();
	}
	
	/**
	 * 更新图书数据的阿文
	 */
	public void updateBookInfo2() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String excelPath = request.getRealPath("/") + "tempPage\\阿文导入表格.xlsx";
			String result = bookService.updateBookInfo2(excelPath);
			out.write(result);
		} catch (Exception e) {
			e.printStackTrace();
			out.write("<p class='text-danger'>对不起，更新失败，失败原因："+e.getMessage()+"</p>");
		}
		out.close();
	}
	
	/**
	 * 历史数据归档
	 * 1. 没有责编或PM文件打不开的归档到离退休的责编目录下
	 * 2. 有责编的找到对应的用户，归档
	 */
	public void historyBookArchive() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		log.debug("历史数据归档开始。。。");
		try {
			out = response.getWriter();
			String result = bookService.historyBookArchive();
			out.print(result);
		}catch (Exception e) {
			e.printStackTrace();
			out.write("<p class='text-danger'>归档错误，原因："+e.getMessage()+"</p>");
			log.error(e);
		}
		log.debug("历史数据归档结束。。。");
		out.close();
	}
	
	/**
	 * 补充采集系统中有目录，ftp中没有的目录
	 * @throws Exception
	 */
	public void createFtpFold2() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String result = bookService.createFtpFold2();
		out.write(result);
		out.close();
	}
	
	/**
	 * FTP建立"电子书封面"和"样章"文件夹
	 * @throws Exception
	 */
	public void createFtpFold3() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bookService.createFtpFold3();
		out.write("1");
		out.close();
	}

	/**
	 * 图书字段生成xml映射
	 * @throws Exception
	 */
	public void getBookInfoXml() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(ColumnMap.getTableColumnXml());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("BookAction ## getBookInfoXml 异常：" + e.getMessage());
			throw e;
		} finally {
			out.close();
		}
	}

	/**
	 * 读取excel，获取工作簿名
	 * @throws Exception
	 */
	public void getSheetsByBookInfoExcel() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String excelPath = request.getRealPath("/")+"tempPage\\图书信息更新.xlsx";
			String tt = bookService.getSheetsByBookInfoExcel(excelPath);
			out.print(tt);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BookAction ## getSheetsByBookInfoExcel 异常：" + e.getMessage());
			throw e;
		} finally {
			out.close();
		}
	}

	public void updateBookInfoByExcel() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String excelPath = request.getRealPath("/")+"tempPage\\图书信息更新.xlsx";
			String myXml = StringUtil.ObjectToString(request.getParameter("myXml"));
			String tt = bookService.updateBookInfoByExcel(excelPath, myXml);
			out.print(tt);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BookAction ## updateBookInfoByExcel 异常：" + e.getMessage());
			throw e;
		} finally {
			out.close();
		}
	}

	/**
	 * 打印word前线验证责编是否已经上传文件 add 2015-05-03
	 * @throws Exception
	 */
	public void checkFileUpload() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			int bookId = StringUtil.StringToInt(request.getParameter("bId"));
			if(bookId==0){ //未获取到图书ID
				out.print("-2");
			} else{
				BookEntity be = bookService.getBookEntityById(bookId);
				if(be==null) { //根据图书ID，没有找到图书信息
					out.print("-1");
				} else {
					out.print(bookService.checkFileUpload(be));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BookAction ## checkFileUpload 异常：" + e.getMessage());
			throw e;
		} finally {
			out.close();
		}
	}

	/**
	 * 图书语种饼状图
	 * @throws Exception
	 */
	public void bookLanDoughnutChart() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String jsonPic = bookService.getBookLanDoughnutChartJson();
			out.print(jsonPic);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BookAction ## bookLanPic 异常：" + e.getMessage());
			throw e;
		} finally {
			out.close();
		}
	}

	/**
	 * 图书出版时间柱形图
	 * @throws Exception
	 */
	public void bookPublishTimeChart() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String time = StringUtil.ObjectToString(request.getParameter("bookPublishTime"));
			if("".equals(time)) {
				time = (new Date().getYear()+1900)+"";
			}
			String jsonPic = bookService.getBookPublishTimeBarChartJson(time);
			out.print(jsonPic);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BookAction ## bookPublishTimeChart 异常：" + e.getMessage());
			throw e;
		} finally {
			out.close();
		}
	}

	/**
	 *根据责编查询图书列表
	 * @return
	 * @throws Exception
	 */
	public String bookListByEditor() throws Exception {
		try {
			// 获取用户该显示的字段，如果没有就显示默认的5个字段，否则由用户自己指定
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
			if (userEntity == null) { // session不存在，返回登录页面
				return "noLogin";
			} else {
				showColumn = configService.getConfigColumnXmlByUserId(userEntity.getUser_id());
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error("BookAction ## bookListByEditor 异常：" + ex.getMessage());
			throw ex;
		}
	}

	/**
	 * 根据条件查询图书列表信息
	 */
	public void getBookListByConditionAndUserId() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		int pageNum = StringUtil.StringToInt(request.getParameter("page"));
		String searchType = StringUtil.ObjectToString(request.getParameter("searchType"));
		String searchContent = StringUtil.ObjectToString(request.getParameter("searchContent"));
		int userId = StringUtil.StringToInt(request.getParameter("userId"));

		String hql = "";
		String countHql = "";
		if (pageNum == 0) {
			pageNum = 1;
		}
		if ("".equals(searchType) || "".equals(searchContent)) {
			if (userId == 0) {
				hql = "from BookEntity where book_del_flag=0 order by book_id desc";
				countHql = "select count(book_id) from wz_book where book_del_flag=0 order by book_id desc";
			} else {
				hql = "from BookEntity where user_id = " + userId + " and book_del_flag=0 order by book_id desc";
				countHql = "select count(book_id) from wz_book where user_id = " + userId + " and book_del_flag=0 order by book_id desc";
			}
		} else {
			// mysql 转义
			searchContent = bookService.repMySqlChar(searchContent);
			String speChar = searchContent.contains("{") ? "#}" : ""; // mysql
			String columnName = ColumnMap.getBookTableColumnNameByCn(searchType);

			if("book_publish_time".equals(columnName)) { //出版时间
				if(searchContent.contains(" 到 ")) { //有起始日期，也有结束日期
					if (userId == 0) {
						hql = "from BookEntity where book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc";
					} else {
						hql = "from BookEntity where user_id = " + userId + " and book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where user_id = " + userId + " and book_publish_time between '"+searchContent.split(" 到 ")[0]+"' and '"+searchContent.split(" 到 ")[1]+"' and book_del_flag = 0 order by book_id desc";
					}
				} else { //只有起始日期
					if (userId == 0) {
						hql = "from BookEntity where book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc";
					} else {
						hql = "from BookEntity where user_id = " + userId + " and book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc ";
						countHql = "select count(book_id) from wz_book where user_id = " + userId + " and book_publish_time = '" + searchContent.trim() + "' and book_del_flag = 0 order by book_id desc";
					}
				}
			} else {
				if (userId == 0) {
					hql = "from BookEntity where " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
					countHql = "select count(book_id) from wz_book where " + columnName + " like '%" + searchContent.trim() + "%' " + speChar + " and book_del_flag = 0 order by book_id desc";
				} else {
					hql = "from BookEntity where user_id = " + userId + " and " + columnName + " like '%" + searchContent.trim() + "%' and book_del_flag = 0 order by book_id desc ";
					countHql = "select count(book_id) from wz_book where user_id = " + userId + " and " + columnName + " like '%" + searchContent.trim() + "%' " + speChar + " and book_del_flag = 0 order by book_id desc";
				}
			}
		}
		System.out.println(hql);
		List<BookEntity> bookList = bookService.findPageFromBook(pageNum, ConfigInfo.PAGE_ROW_COUNT, hql);
		int pageCount = bookService.getPageCount(countHql, ConfigInfo.PAGE_ROW_COUNT);
		PageEntity pageEntity = new PageEntity();
		pageEntity.setCurrentPage(pageNum);
		pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
		pageEntity.setRowCount(0);
		pageEntity.setPageCount(pageCount);
		map.put("bookList", bookList);
		map.put("pageEntity", pageEntity);
		JSONObject json = JSONObject.fromObject(map);// 将map对象转换成json类型数据
		out.print(json.toString());
		out.flush();
		out.close();
	}
}
