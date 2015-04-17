package com.wz.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wz.util.StringUtil;

public class Test4 {
	
	
	public String getSheets(String excelPath) throws Exception{
		String result="";
		File excelFile = new File(excelPath);
		if(!excelFile.exists()) {
			result = "<p style='color:red'>excel路径有误："+excelPath+"</p>";
		} else {
			try {
				InputStream in = new FileInputStream(excelFile);
				XSSFWorkbook wb = new XSSFWorkbook(in);
				for(int i = 0; i < wb.getNumberOfSheets();i++) {
					XSSFSheet sheet = wb.getSheetAt(i);
					String sheetName = sheet.getSheetName();
					if(sheetName.contains("_")) {
						result+="<div class='col-sm-4'><input type='checkbox' value='"+i+"'>"+sheetName+"</div>";
					}
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
		
		return result;
	}

	
	public String updateBookOnlineStatusBySheetIds(String excelPath, String sheetIds) throws Exception{
		String result = "";
		File excelFile = new File(excelPath);
		if(!excelFile.exists()) {
			result = "<p style='color:red'>excel路径有误："+excelPath+"</p>";
		} else {
			try {
				InputStream in = new FileInputStream(excelFile);
				XSSFWorkbook wb = new XSSFWorkbook(in);
				for(int i = 0; i < sheetIds.split(",").length;i++) {
					XSSFSheet sheet = wb.getSheetAt(Integer.parseInt(sheetIds.split(",")[i]));
					int osId = StringUtil.StringToInt((sheet.getSheetName().split("_")[1]));
					int rowCount = sheet.getLastRowNum();
					if(rowCount>1) {
						for(int j = 1; j < rowCount; j++) {
							XSSFRow row = sheet.getRow(j);
							String book_isbn = getCellValue(row.getCell(0));
							int is_online = StringUtil.StringToInt(getCellValue(row.getCell(6)));
							System.out.println(osId+"=="+book_isbn+"=="+is_online);
						}
					}
					if(i==3)break;
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
		return result;
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
				System.out.println("==");
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
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		String excelPath = "E:\\Workspaces\\MyEclipse Professional 2014\\wuzhou\\WebRoot\\tempPage\\图书各平台状态标注.xlsx";
		Test4 t = new Test4();
		System.out.println(t.updateBookOnlineStatusBySheetIds(excelPath, "3,4,5,6,7,8,9,10"));

	}

}
