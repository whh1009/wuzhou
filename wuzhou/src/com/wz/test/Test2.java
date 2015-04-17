package com.wz.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wz.common.ConfigInfo;
import com.wz.util.FileUtil;
import com.wz.util.StringUtil;

public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
//		new File("e:\\ftp\\201409之前书目\\B_EP_9787508523835_002_mRCoY").delete();
//		String path = "E:\\ftp\\201409之前书目\\B_EP_9787222_001_E15n1\\印刷PDF";
//		File file = new File(path);
//		file.renameTo(new File())
		rename();
	}
	
	public static void rename() throws Exception {
		String ftp_root = "e:\\ftp";
		File [] files = new File(ftp_root).listFiles();
		for(File file : files) {
			File [] subFiles = file.listFiles();
			for(File subFile : subFiles) {
				File [] subSubFiles = subFile.listFiles();
				for(File subSubFile : subSubFiles) {
					String filePath = subSubFile.getAbsolutePath();
					if(filePath.endsWith("印刷PDF")) {
						subSubFile.renameTo(new File(filePath.replace("印刷PDF", "分层PDF")));
					}
				}
			}
		}
	}
	
	public static void scanFold() throws FileNotFoundException, IOException {
//		String epubFold = ConfigInfo.EBOOK_ROOT_PATH+"\\EPUB";
//		String mobiFold = ConfigInfo.EBOOK_ROOT_PATH+"\\MOBI";
//		String pdfFold = ConfigInfo.EBOOK_ROOT_PATH+"\\PDF";
//		String jpgFold = ConfigInfo.EBOOK_ROOT_PATH+"\\JPG";
		Map<String, File> map = new HashMap<String, File>();
		File [] files = new File(ConfigInfo.EBOOK_ROOT_PATH).listFiles();
		if(files!=null&&files.length!=0) {
			for(File file : files) {
				String fileName = file.getName();
				map.put(fileName, file);
			}
		}
		System.out.println("共扫描到 "+map.size()+" 个文件");
		
		
		String excelPath = ConfigInfo.EBOOK_ROOT_PATH + "\\元数据.xlsx";
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(excelPath)));
		XSSFSheet sheet = wb.getSheetAt(0);
		int rowCount = sheet.getLastRowNum();
		for (int i = 0; i < rowCount; i++) {
			XSSFRow row = sheet.getRow(i);
			XSSFCell isbnCell = row.getCell(0);
			XSSFCell bookNameCell = row.getCell(1);
			String isbn = getCellValue(isbnCell);
			String bookName = getCellValue(bookNameCell);
			int bookCount = 0; //读取excel，判断一个isbn有几个对应的文件夹，超过1个就报警
			
			
		}
		
	}

	public static void paserExcel() throws Exception {
		//List<String> isbnList = getBookIsbns();
		File [] files = new File(ConfigInfo.FTP_ROOT+"\\201409之前书目").listFiles();
		String excelPath = ConfigInfo.EBOOK_ROOT_PATH + "\\元数据.xlsx";
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(excelPath)));
		XSSFSheet sheet = wb.getSheetAt(0);
		int rowCount = sheet.getLastRowNum();
		
		for (int i = 0; i < rowCount; i++) {
			XSSFRow row = sheet.getRow(i);
			XSSFCell isbnCell = row.getCell(0);
			XSSFCell bookNameCell = row.getCell(1);
			String isbn = getCellValue(isbnCell);
			String bookName = getCellValue(bookNameCell);
			int bookCount = 0; //读取excel，判断一个isbn有几个对应的文件夹，超过1个就报警
			for(File file : files) {
				String fileName = file.getName();
				if(fileName.split("_").length>2) {
					String ib = fileName.split("_")[2];
					if(isbn.equals(ib)) {
						bookCount++;
					}
				}
			}
			if(bookCount>=2) {
				System.out.println("错误：共发现"+bookCount+"个相同ISBN的书，需要检查文种，ISBN:"+isbn+" 书名："+bookName);
			} else {
				File ebookFile = new File(ConfigInfo.EBOOK_ROOT_PATH+"\\"+bookName+".epub");
				if(ebookFile!=null) {
					
				}
			}
		}
	}
	
	private static List<String> getBookIsbns() {
		File [] files = new File(ConfigInfo.FTP_ROOT+"\\201409之前书目").listFiles();
		List<String> out = new ArrayList<String>();
		for(File file : files) {
			String fileName = file.getName();
			if(fileName.split("_").length>2) {
				out.add(fileName.split("_")[2]);
			}
		}
		return out;
	}
	

	private static String getCellValue(XSSFCell cell) {
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
		default:
			cellValue = "";
		}
		return cellValue;
	}
	
	

}
