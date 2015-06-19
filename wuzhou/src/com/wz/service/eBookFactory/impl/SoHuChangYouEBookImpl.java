package com.wz.service.eBookFactory.impl;

import com.wz.common.ConfigInfo;
import com.wz.entity.BookEntity;
import com.wz.entity.UserEntity;
import com.wz.service.eBookFactory.EBookFormat;
import com.wz.service.eBookFactory.EBookTool;
import com.wz.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 搜狐畅游
 * Created by wanghonghui on 2015/6/18.
 */
public class SoHuChangYouEBookImpl implements EBookFormat{
    private List<BookEntity> bookList;
    private String excelPath;
    @Override
    public void renameEBook(List<BookEntity> bookList, List<UserEntity> userList, String desPath, String excelPath) {
        if(bookList==null||bookList.isEmpty()) return;
        this.bookList = bookList;
        this.excelPath = excelPath;
        for(BookEntity be : bookList) {
            String path = be.getBook_epub_serverpath().replace("/", "\\");
            String bookRootPath = ConfigInfo.FTP_ROOT +"\\" + EBookTool.getUserNameByUserId(be.getUser_id(), userList) +"\\"+ be.getBook_serial_number();
            if(path.startsWith("\\201409之前书目\\")) {
                bookRootPath = ConfigInfo.FTP_ROOT +"\\201409之前书目\\"+ be.getBook_serial_number();
            }
            String epubRootPath = bookRootPath + "\\EPUB";
            EBookTool.copy(epubRootPath, desPath+"\\电子文件", "epub", be.getBook_name_cn());
            String pdfRootPath = bookRootPath + "\\阅读PDF";
            EBookTool.copy(pdfRootPath, desPath+"\\电子文件", "pdf", be.getBook_name_cn());
            String coverRootPath = bookRootPath + "\\电子书封面";
            EBookTool.copy(coverRootPath, desPath+"\\电子书封面", "jpg", be.getBook_name_cn());

            //补充样章
            String yzRootPath = bookRootPath + "\\样章";
            EBookTool.copy(yzRootPath, desPath+"\\样章", "epub", be.getBook_name_cn());
            EBookTool.copy(yzRootPath, desPath+"\\样章", "pdf", be.getBook_name_cn());
        }
    }

    @Override
    public void createExcel(OutputStream out) {
        if(bookList==null||bookList.isEmpty()) return;
        try {
            InputStream in = new FileInputStream(new File(excelPath));
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            int rowNum = 2; //从第3行开始写入数据
            for(BookEntity be : bookList) {
                XSSFRow row = sheet.createRow(rowNum);
                EBookTool.createBaseCell(wb, row, 0, StringUtil.ObjectToString(be.getBook_isbn()), 0);
                EBookTool.createBaseCell(wb, row, 1, StringUtil.ObjectToString(be.getBook_name_cn()), 0);
                EBookTool.createBaseCell(wb, row, 2, StringUtil.ObjectToString(be.getBook_name_xi()), 0);
                EBookTool.createBaseCell(wb, row, 3, StringUtil.ObjectToString(be.getBook_author()), 0);
                EBookTool.createBaseCell(wb, row, 4, StringUtil.ObjectToString(be.getBook_author_xi()), 0);
                EBookTool.createBaseCell(wb, row, 5, "", 1);//类型（分类）？？
                EBookTool.createBaseCell(wb, row, 6, StringUtil.ObjectToString(be.getBook_language().replaceAll("[0-9]{3}[\\-]{2}", "")), 0);
                EBookTool.createBaseCell(wb, row, 7, StringUtil.ObjectToString(be.getBook_content_intr_xi()), 0);
                EBookTool.createBaseCell(wb, row, 8, "有", 0);
                rowNum++;
            }
            wb.write(out);
            out.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
