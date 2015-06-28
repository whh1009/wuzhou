package com.wz.service.eBookFactory.impl;

import com.wz.common.BookLanguageMap;
import com.wz.common.ConfigInfo;
import com.wz.entity.BookEntity;
import com.wz.entity.UserEntity;
import com.wz.service.eBookFactory.EBookFormat;
import com.wz.service.eBookFactory.EBookTool;
import com.wz.util.FileUtil;
import com.wz.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 亚马逊中国 平台
 * @author wanghonghui
 *
 */
public class AmazonCnEBookImpl implements EBookFormat {
	//1. 多个mobi文件，只有一个封面文件时，如何对应之间的关系（命名对应关系）？？？ 分别计数

	private List<BookEntity> bookList;
	private String excelPath;

	private Map<String, String> map = new HashMap<String, String>();//isbn-参考代码映射
	@Override
	public void renameEBook(List<BookEntity> bookList, List<UserEntity> userList, String desPath, String excelPath) { //供应商代码_书号_上传日期（一般也是发售日期）_第几本书
		if(bookList==null||bookList.isEmpty()) return;
		this.bookList = bookList;
		this.excelPath = excelPath;
		int mobiCount = 1, coverCount = 1;
		for(BookEntity be : bookList) {
			String path = be.getBook_mobi_serverpath().replace("/", "\\");
			String bookRootPath = ConfigInfo.FTP_ROOT +"\\" + EBookTool.getUserNameByUserId(be.getUser_id(), userList) +"\\"+ be.getBook_serial_number();
			if(path.startsWith("\\201409之前书目\\")) {
				bookRootPath = ConfigInfo.FTP_ROOT +"\\201409之前书目\\"+ be.getBook_serial_number();
			}
			String mobiRootPath = bookRootPath + "\\MOBI";
			List<File> mobiList = new ArrayList<File>();
			EBookTool.getFileByExtension(mobiRootPath, "mobi", mobiList);
			for(File mobiFile : mobiList) {
				try {
					FileUtil.copyFile(mobiFile, new File(desPath+"\\电子文件\\CNICN_"+be.getBook_isbn()+"_"+StringUtil.dateToString("yyyyMMdd")+"_"+StringUtil.formatNum(mobiCount)+".mobi"), true);
					map.put(be.getBook_isbn(), "CNICN_"+be.getBook_isbn()+"_"+StringUtil.dateToString("yyyyMMdd")+"_"+StringUtil.formatNum(mobiCount));//添加isbn-参考代码映射
				} catch (IOException e) {
					e.printStackTrace();
				}
				mobiCount++;
			}

			String coverRootPath = bookRootPath + "\\电子书封面";
			List<File> coverList = new ArrayList<File>();
			EBookTool.getFileByExtension(coverRootPath, "jpg", coverList);

			for(File coverFile : coverList) {
				try {
					FileUtil.copyFile(coverFile, new File(desPath+"\\电子书封面\\CNICN_"+be.getBook_isbn()+"_"+StringUtil.dateToString("yyyyMMdd")+"_"+StringUtil.formatNum(coverCount)+".jpg"), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				coverCount++;
			}

			//不需要样章

		}
	}

	@Override
	public void createExcel(OutputStream out) {
		if(bookList==null||bookList.isEmpty()) return;
		try {
			InputStream in = new FileInputStream(new File(excelPath));
			XSSFWorkbook wb = new XSSFWorkbook(in);
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowNum = 5; //从第6行开始写入数据
			for(BookEntity be : bookList) {
				XSSFRow row = sheet.createRow(rowNum);
				row.createCell(0).setCellValue("五洲传播出版社");
				row.createCell(1).setCellValue("wuzhouchuanbochubanshe");
				row.createCell(2).setCellValue("五洲传播出版社");
				row.createCell(3).setCellValue(StringUtil.ObjectToString(be.getBook_name_cn()));
				row.createCell(4).setCellValue(StringUtil.getPinYin(be.getBook_name_cn()));
				row.createCell(5).setCellValue(StringUtil.ObjectToString(be.getBook_author()));
				row.createCell(6).setCellValue(StringUtil.getPinYin(be.getBook_author()));
				row.createCell(7).setCellValue(getCkdm(be.getBook_isbn()));
				row.createCell(9).setCellValue(StringUtil.ObjectToString(be.getBook_isbn()));
				row.createCell(10).setCellValue("第"+be.getBook_publish_count()+"版");
				row.createCell(11).setCellValue(StringUtil.dateToString("yyyyMMdd"));
				row.createCell(12).setCellValue(getTime(be.getBook_publish_time()));
				row.createCell(13).setCellValue(StringUtil.ObjectToString(be.getBook_content_intr_cn()));
				row.createCell(17).setCellValue(StringUtil.ObjectToString(be.getBook_clcc()));
				row.createCell(18).setCellValue(BookLanguageMap.getAmazonLanguage(be.getBook_language()));
				row.createCell(19).setCellValue(StringUtil.ObjectToString(be.getBook_paper_price()));
				row.createCell(20).setCellValue("CNY");
				row.createCell(23).setCellValue(StringUtil.ObjectToString(be.getBook_series_cn()));
				row.createCell(30).setCellValue(StringUtil.ObjectToString(be.getBook_keyword_cn()));
				rowNum++;
			}
			wb.write(out);
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * 获取参考代码
	 * @param isbn
	 * @return
	 */
	private String getCkdm(String isbn) {
		if(map.get(isbn)==null) return "";
		else return map.get(isbn).toString();
	}

	private String getTime(String time) {
		time = StringUtil.ObjectToString(time);
		return time.length()==0?"":time.replace("-", "")+"01";
	}

	public static void main(String[] args){
		Map<String, String> m = new HashMap<String, String>();//isbn-参考代码映射
//		m.put("123","456");
		System.out.println(m.get("12"));
	}
}
