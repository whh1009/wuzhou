package com.wz.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.wz.common.ColumnMap;
import com.wz.common.ConfigInfo;
import com.wz.common.PageEntity;
import com.wz.entity.*;
import com.wz.service.BookService;
import com.wz.service.ConfigService;
import com.wz.service.EBookManService;
import com.wz.service.UserService;
import com.wz.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 格式化各平台名称，That's Books on China/Over Drive/Amazon
 * 
 * @author Administrator
 * 
 */
public class EBookManAction extends ActionSupport {
	ConfigService configService;
	EBookManService eBookManService;
	BookService bookService;
	UserService userService;
	String showColumn;
	String osXml; //在线平台的xml<os><item osId="1" osName="亚马逊（中国）" /></os>
	BookOnlineOSEntity bookOnlineOSEntity;
	List<BookOnlineOSEntity> osList; //电子书平台列表
	public EBookManAction() {
		configService = new ConfigService();
		eBookManService = new EBookManService();
		bookService = new BookService();
		userService = new UserService();
	}

	/**
	 * 首页，图书列表
	 * 
	 * @return
	 */
	public String index() {
		try {
			// 获取用户该显示的字段，如果没有就显示默认的5个字段，否则由用户自己指定
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
			if (userEntity == null) { // session不存在，返回登录页面
				return "noLogin";
			} else {
				showColumn = configService.getConfigColumnXmlByUserId(userEntity.getUser_id());
			}
			System.out.println(showColumn);
			return Action.SUCCESS;
		} catch (Exception ex) {
			return Action.ERROR;
		}
	}

	/**
	 * 电子书平台管理
	 * @return
	 */
	public String eBookOSMan() {
		try {
			osList = eBookManService.getBookOnlineOS();
			return Action.SUCCESS;
		} catch (Exception ex) {
			return Action.ERROR;
		}
	}
	
	/**
	 * 添加电子书平台
	 * @return
	 */
	public String addEBookOS() {
		try{
			if(eBookManService.addEBookOS(bookOnlineOSEntity)) {
				return Action.SUCCESS;
			} else {
				return Action.ERROR;
			}
		} catch(Exception ex) {
			return Action.ERROR;
		}
	}
	
	/**
	 * 删除
	 * @throws Exception
	 */
	public void deleteEBookOs() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		int osId = StringUtil.StringToInt(request.getParameter("osId"));
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(osId==0) { //
			out.write("获取平台序号出错！");
		} else {
			if(eBookManService.deleteEBookOS(osId)) {
				out.write("删除成功");
			} else {
				out.write("删除出错！");
			}
		}
		out.close();
	}
	
	public void updateEBookOS() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		int osId = StringUtil.StringToInt(request.getParameter("osId"));
		String osName = StringUtil.ObjectToString(request.getParameter("osName"));
		String osDesc = StringUtil.ObjectToString(request.getParameter("osDesc"));
		int osFlag = StringUtil.StringToInt(request.getParameter("osFlag"));
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(osId==0||osName.length()==0) { //
			out.write("获取平台序号或平台名出错！");
		} else {
			BookOnlineOSEntity osEntity = new BookOnlineOSEntity();
			osEntity.setOs_id(osId);
			osEntity.setOs_name(osName);
			osEntity.setOs_desc(osDesc);
			osEntity.setOs_flag(osFlag);
			if(eBookManService.updateEBookOS(osEntity)) {
				out.write("修改成功");
			} else {
				out.write("修改出错！");
			}
		}
		out.close();
	}
	
	/**
	 * 电子书上线管理
	 * @return
	 */
	public String eBookOnlineMan() {
		try {
			// 获取用户该显示的字段，如果没有就显示默认的5个字段，否则由用户自己指定
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
			if (userEntity == null) { // session不存在，返回登录页面
				return "noLogin";
			} else {
				showColumn = configService.getConfigColumnXmlByUserId(userEntity.getUser_id());
				osXml = eBookManService.getOsXml();
				System.out.println("os:"+osXml);
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			return Action.ERROR;
		}
	}
	
	public void getBookOnlineListByCondition() throws Exception {
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
		List<BookOnlineEntity> bookOnlineList = eBookManService.getOnlineOsList(bookList);
		int pageCount = bookService.getPageCount(countHql, ConfigInfo.PAGE_ROW_COUNT);
		PageEntity pageEntity = new PageEntity();
		pageEntity.setCurrentPage(pageNum);
		pageEntity.setPageRowCount(ConfigInfo.PAGE_ROW_COUNT);
		pageEntity.setRowCount(0);
		pageEntity.setPageCount(pageCount);
		map.put("bookList", bookList);
		map.put("pageEntity", pageEntity);
		map.put("bookOnlineList", bookOnlineList);
		JSONObject json = JSONObject.fromObject(map);// 将map对象转换成json类型数据
		out.print(json.toString());
		out.flush();
		out.close();
	}
	
	public void updateBookOnline() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String markStr = StringUtil.ObjectToString(request.getParameter("markStr"));
		if("".equals(markStr)) { //
			out.write("-1");
		} else {
			if(eBookManService.updateBookOnlineByXml(markStr)==1) {
				out.write("1");
			} else {
				out.write("0");
			}
		}
		out.close();
	}
	
	/**
	 * 根据图书ID 获取平台信息
	 * @throws Exception
	 */
	public void getOnlineOsByBookId() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		int bookId = StringUtil.StringToInt(request.getParameter("bookId"));
		if(bookId==0) { //没有获取到图书id
			out.write("-1");
		} else {
			out.write(eBookManService.getOnlineOsByBookId(bookId));
		}
		out.close();
	}
	
	/**
	 * 格式化各平台电子书
	 * @return
	 */
	public String formatEBook () {
		try {
			// 获取用户该显示的字段，如果没有就显示默认的5个字段，否则由用户自己指定
			HttpServletRequest request = ServletActionContext.getRequest();
			UserEntity userEntity = (UserEntity) request.getSession().getAttribute("userEntity");
			if (userEntity == null) { // session不存在，返回登录页面
				return "noLogin";
			} else {
				showColumn = configService.getConfigColumnXmlByUserId(userEntity.getUser_id());
			}
			System.out.println(showColumn);
			return Action.SUCCESS;
		} catch (Exception ex) {
			return Action.ERROR;
		}
	}

	/**
	 * 根据图书id，获取图书信息
	 * @throws Exception 
	 */
	public void getBookListByBookIds() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		String bookIds = StringUtil.ObjectToString(request.getParameter("bookIds"));
		PrintWriter out = response.getWriter();
		if ("".equals(bookIds)) { //没有获取到bookId
			out.print("0");
		} else {
			bookIds = StringUtil.ignoreComma(bookIds);
			List<BookEntity> bookList = bookService.getBookListByHql("from BookEntity where book_id in ( "+bookIds+" )");
			JSONArray json = JSONArray.fromObject(bookList);// 将map对象转换成json类型数据
			out.print(json.toString());
		}
		out.close();
	}
	
	/**
	 * 格式化单本书或多本到各平台
	 * @throws Exception
	 */
	public void formatEBookByBookId() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		String bookIds = StringUtil.ObjectToString(request.getParameter("bookIds"));
		PrintWriter out = response.getWriter();
		if ("".equals(bookIds)) { //没有获取到bookId
			out.print("0");
		} else {
			List<UserEntity> userList = userService.getUserList();
			bookIds = StringUtil.ignoreComma(bookIds);
			List<BookEntity> bookList = bookService.getBookListByHql("from BookEntity where book_id in ( "+bookIds+" )");
			String excelPath = request.getRealPath("/")+"formatOS\\temp";
			boolean flag = eBookManService.formatEBook(bookList, userList, excelPath);
			if(flag) { //格式化成功
				out.print("1");
			} else { //格式化失败
				out.print("2");
			}
		}
		out.close();
	}
	
	/**
	 * 导入存量资源
	 */
	public String importEBook() {
		return "success";
	}
	
	public void getEBookFileList() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(eBookManService.getEBookFileList());
		out.close();
	}
	
	/**
	 * 复制电子书到存量资源目录下
	 * @throws Exception
	 */
	public void moveEBooks() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String excelPath = ConfigInfo.EBOOK_ROOT_PATH+"\\元数据.xlsx";
		List<YuanDataEntity> list = eBookManService.loadYuanDataExcel(excelPath);
		if(list==null||list.isEmpty()) {
			out.print("<p style='color:red'>Excel未找到，或Excel中没有数据</p>");
		} else {
			out.print(eBookManService.moveEBooks(list));
		}
		out.close();
	}

	public void moveYZEBooks() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		File excelFile = new File(ConfigInfo.EBOOK_YZ_PATH+"\\元数据.xlsx");
		if(excelFile.exists()) {
			out.print(eBookManService.moveYZEBooks(excelFile));
		} else {
			out.print("<p style='color:red'>Excel未找到，或Excel中没有数据</p>");
		}
		out.close();
	}
	
	/**
	 * 更新图书价格
	 * @throws Exception
	 */
	public void updateBookPrice() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String excelPath = request.getRealPath("/")+"tempPage\\电子书价格.xlsx";
		String tt = eBookManService.updateBookPrice(excelPath);
		out.print(tt);
		out.close();
	}
	
	/**
	 * 更新电子书流程状态
	 * @throws Exception
	 */
	public void updateOnlineStatus() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String result = StringUtil.ObjectToString(request.getParameter("result"));
		System.out.println("=="+result+"==");
		int tt = eBookManService.updateOnlineStatus(result);
		out.print(tt);
		out.close();
	}
	
	/**
	 * 根据在线码获得图书列表
	 * @throws Exception
	 */
	public void getOnlineStatusBook() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		int osIdSel = StringUtil.StringToInt(request.getParameter("osIdSel"));
		int statusIdSel = StringUtil.StringToInt(request.getParameter("statusIdSel"));
		//int currentPage = StringUtil.StringToInt(request.get);//页码
		List<BookOnlineEntity> bookOnlineList = eBookManService.getOnlineStatusBook(osIdSel, statusIdSel);
		List<BookEntity> bookEntityList = new ArrayList<BookEntity>();
		if(bookOnlineList==null) {
			out.print("");
			return;
		} else {
			Set<Integer> set = new HashSet<Integer>();
			for(BookOnlineEntity boe : bookOnlineList) {
				set.add(boe.getBook_id());
			}
			String ids = "";
			for(int id : set) {
				ids +=id+",";
			}
			ids = StringUtil.ignoreComma(ids);
			if("".equals(ids)) {
				out.print("");
				return;
			} else {
				bookEntityList = bookService.getBookListByHql("from BookEntity where book_id in (" + ids + ") ");
			}
		}
		Map map = new HashMap();
		map.put("bookOnlineList", bookOnlineList);
		map.put("bookList", bookEntityList);
		JSONObject json = JSONObject.fromObject(map);// 将map对象转换成json类型数据
		out.print(json.toString());
		out.close();
	}
	
	/**
	 * 更新图书在线状态，获取excel的sheet名
	 * @throws Exception
	 */
	public void getSheetsByOnlineStatus() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String excelPath = request.getRealPath("/")+"tempPage\\图书各平台状态标注.xlsx";
		String result = eBookManService.getSheetsByOnlineStatus(excelPath);
		out.print(result);
		out.close();
	}
	
	/**
	 * 标注图书状态，根据sheet的id来更新图书状态
	 * @throws Exception
	 */
	public void updateBookOnlineStatusBySheetIds() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String sheetIds = StringUtil.ObjectToString(request.getParameter("sheetIds"));
		if("".equals(sheetIds)) { //未获取sheet的ID
			out.print("-1");
			return;
		}
		sheetIds = StringUtil.ignoreComma(sheetIds);
		String excelPath = request.getRealPath("/")+"tempPage\\图书各平台状态标注.xlsx";
		String result = eBookManService.updateBookOnlineStatusBySheetIds(excelPath, sheetIds);
		out.print(result);
		out.close();
	}
	
	/**
	 * 更新图书价格
	 * @throws Exception
	 */
	public void updateBookPrice2() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String excelPath = request.getRealPath("/")+"tempPage\\电子书价格2.xlsx";
		String tt = eBookManService.updateBookPrice2(excelPath);
		out.print(tt);
		out.close();
	}

	public void getSheetsByBookInfoExcel() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//String excelPath = request.getRealPath("/")+"tempPage\\电子书价格2.xlsx";
		//String tt = eBookManService.updateBookPrice2(excelPath);
		//out.print(tt);
		out.close();
	}

	//图书状态导出
	public void exportEBookOnline() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String bookLan = StringUtil.ObjectToString(request.getParameter("bookLan"));
		int osId = StringUtil.StringToInt(request.getParameter("osId"));
		int osStatus = StringUtil.StringToInt(request.getParameter("osStatus"));

//		select b2.* from wz_book b1, wz_book_online b2
//		where b1.book_del_flag=0
//		and b2.book_id = b1.book_id
//
//		and b1.book_language = '002--西文'
//		and b2.os_id = 1
//		and b2.is_online = 1

		out.print("");
		out.close();
	}

	
	public String getShowColumn() {
		return showColumn;
	}

	public void setShowColumn(String showColumn) {
		this.showColumn = showColumn;
	}

	public List<BookOnlineOSEntity> getOsList() {
		return osList;
	}

	public void setOsList(List<BookOnlineOSEntity> osList) {
		this.osList = osList;
	}

	public BookOnlineOSEntity getBookOnlineOSEntity() {
		return bookOnlineOSEntity;
	}

	public void setBookOnlineOSEntity(BookOnlineOSEntity bookOnlineOSEntity) {
		this.bookOnlineOSEntity = bookOnlineOSEntity;
	}

	public String getOsXml() {
		return osXml;
	}

	public void setOsXml(String osXml) {
		this.osXml = osXml;
	}
	
	
}
