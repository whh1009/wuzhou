package com.wz.service.eBookFactory.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wz.common.BookLanguageMap;
import com.wz.common.ConfigInfo;
import com.wz.entity.BookEntity;
import com.wz.entity.UserEntity;
import com.wz.service.eBookFactory.EBookFormat;
import com.wz.service.eBookFactory.EBookTool;
import com.wz.util.FileUtil;
import com.wz.util.StringUtil;

/**
 * OverDrive 平台
 * @author Administrator
 *
 */
public class OverDriveEBookImpl implements EBookFormat {
	private List<BookEntity> bookList;
	private String excelPath;
	@Override
	public void renameEBook(List<BookEntity> bookList, List<UserEntity> userList, String desPath, String excelPath) { //中文书名
		if(bookList==null||bookList.isEmpty()) return;
		this.bookList = bookList;
		this.excelPath = excelPath;
		for(BookEntity be : bookList) {
			String path = be.getBook_mobi_serverpath().replace("/", "\\");
			String bookRootPath = ConfigInfo.FTP_ROOT +"\\" + EBookTool.getUserNameByUserId(be.getUser_id(), userList) +"\\"+ be.getBook_serial_number();
			if(path.startsWith("\\201409之前书目\\")) {
				bookRootPath = ConfigInfo.FTP_ROOT +"\\201409之前书目\\"+ be.getBook_serial_number();
			}
			String epubRootPath = bookRootPath + "\\EPUB";
			EBookTool.copy(epubRootPath, desPath, "epub", be.getBook_isbn());
			String pdfRootPath = bookRootPath + "\\阅读PDF";
			EBookTool.copy(pdfRootPath, desPath, "pdf", be.getBook_isbn());
			String coverRootPath = bookRootPath + "\\封面";
			EBookTool.copy(coverRootPath, desPath, "jpg", be.getBook_isbn());
		}
	}
	
	@Override
	public void createExcel(OutputStream out) {
		if(bookList==null||bookList.isEmpty()) return;
		try {
			InputStream in = new FileInputStream(new File(excelPath));
			XSSFWorkbook wb = new XSSFWorkbook(in);
			XSSFSheet sheet = wb.getSheetAt(1);
			int rowNum = 3; //从第4行开始写入数据
			for(BookEntity be : bookList) {
				XSSFRow row = sheet.createRow(rowNum);
				String wenzhong = StringUtil.ObjectToString(be.getBook_language());
				if("003--中文".equals(wenzhong)){
					row.createCell(0).setCellValue(StringUtil.ObjectToString(be.getBook_name_cn()));
					row.createCell(3).setCellValue(StringUtil.ObjectToString(be.getBook_series_cn()));
				} else if("500--双语对应".equals(wenzhong)) {
					row.createCell(0).setCellValue((StringUtil.ObjectToString(be.getBook_name_cn())+"（"+StringUtil.ObjectToString(be.getBook_bilingual())+"）").replace("（无）", "").replace("（）", ""));
					row.createCell(3).setCellValue((StringUtil.ObjectToString(be.getBook_series_cn())+"（"+StringUtil.ObjectToString(be.getBook_series_foreign())+"）").replace("（无）", "").replace("（）", ""));
				} else if("001--英文".equals(wenzhong)){
					row.createCell(0).setCellValue((StringUtil.ObjectToString(be.getBook_name_cn())+"（"+StringUtil.ObjectToString(be.getBook_name_english())+"）").replace("（无）", "").replace("（）", ""));
					row.createCell(3).setCellValue((StringUtil.ObjectToString(be.getBook_series_cn())+"（"+StringUtil.ObjectToString(be.getBook_series_english())+"）").replace("（无）", "").replace("（）", ""));
				} else {
					row.createCell(0).setCellValue((StringUtil.ObjectToString(be.getBook_name_cn())+"（"+StringUtil.ObjectToString(be.getBook_name_foreign())+"）").replace("（无）", "").replace("（）", ""));
					row.createCell(3).setCellValue((StringUtil.ObjectToString(be.getBook_series_cn())+"（"+StringUtil.ObjectToString(be.getBook_series_foreign())+"）").replace("（无）", "").replace("（）", ""));
				}
				row.createCell(1).setCellValue("");
				row.createCell(2).setCellValue("1"); //Edition??
				
				String authors = be.getBook_author();
				if(StringUtil.ObjectToString(authors).length()!=0) {
					String author [] = authors.replaceAll("[；，、,]", ";").split(";");
					for(int i=0;i<author.length;i++) {
						row.createCell(i+4).setCellValue(StringUtil.getUpEname(be.getBook_author()));
						row.createCell(i+5).setCellValue("");
						row.createCell(i+6).setCellValue("Author");
						if(i==3) break;
					}
				}
				row.createCell(16).setCellValue("China Intercontinental Press");
				row.createCell(17).setCellValue(""); //Imprint
				row.createCell(18).setCellValue(StringUtil.ObjectToString(be.getBook_isbn()));
				row.createCell(19).setCellValue(StringUtil.ObjectToString(be.getBook_isbn()));
				row.createCell(20).setCellValue(""); //UPC/CatalogID
				String time = StringUtil.ObjectToString(be.getBook_publish_time());
				row.createCell(21).setCellValue(time.split("-")[1]+"/01/"+time.split("-")[0]);
				row.createCell(22).setCellValue(StringUtil.ObjectToString(be.getBook_ebook_dollar_price()));
				row.createCell(23).setCellValue(StringUtil.ObjectToString(be.getBook_ebook_dollar_price()));
				row.createCell(24).setCellValue("USD");
				row.createCell(25).setCellValue("");
				row.createCell(26).setCellValue(StringUtil.dateToString("dd/MM/yyy"));
				row.createCell(27).setCellValue(BookLanguageMap.getOverDriveLanguage(be.getBook_language()));
				row.createCell(28).setCellValue("World");
				row.createCell(29).setCellValue("");//Subject1
				row.createCell(30).setCellValue("");//Subject2
				row.createCell(31).setCellValue("");//Subject3
				row.createCell(32).setCellValue("");//Subject4
				row.createCell(33).setCellValue("");//BISAC
				row.createCell(34).setCellValue("");//BIC
				row.createCell(35).setCellValue(StringUtil.ObjectToString(be.getBook_keyword_english()));//keyWords
				row.createCell(36).setCellValue((StringUtil.ObjectToString(be.getBook_content_intr_cn())+"（"+StringUtil.ObjectToString(be.getBook_content_intr_english()+"）")).replace("（null）","").replace("（无）",""));
				
				row.createCell(39).setCellValue(be.getBook_isbn()+".jpg");//cover
				
				row.createCell(49).setCellValue("N");
				row.createCell(50).setCellValue("N");
				row.createCell(51).setCellValue("N");
				row.createCell(52).setCellValue("N");
				row.createCell(53).setCellValue(be.getBook_isbn()+".pdf");
				row.createCell(54).setCellValue(be.getBook_isbn()+".epub");
				rowNum++;
			}
			wb.write(out);
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
}
