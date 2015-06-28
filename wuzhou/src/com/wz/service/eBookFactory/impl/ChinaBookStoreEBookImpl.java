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
 * China Book Store
 * Created by wanghonghui on 2015/6/18.
 */
public class ChinaBookStoreEBookImpl implements EBookFormat{
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
            EBookTool.copy(epubRootPath, desPath+"\\样章", "epub", be.getBook_name_cn());
            EBookTool.copy(pdfRootPath, desPath+"\\样章", "pdf", be.getBook_name_cn());
        }
    }

    @Override
    public void createExcel(OutputStream out) {
        if(bookList==null||bookList.isEmpty()) return;
        try {
            InputStream in = new FileInputStream(new File(excelPath));
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            int rowNum = 1; //从第2行开始写入数据
            for(BookEntity be : bookList) {
                XSSFRow row = sheet.createRow(rowNum);
                String bookPublishTime = StringUtil.ObjectToString(be.getBook_publish_time());
                if(!"".equals(bookPublishTime)) {
                    EBookTool.createBaseCell(wb, row, 0, StringUtil.ObjectToString(be.getBook_publish_time())+"-01", 0);
                }
                EBookTool.createBaseCell(wb, row, 1, StringUtil.ObjectToString(be.getBook_publish_count()), 0);
                EBookTool.createBaseCell(wb, row, 2, StringUtil.ObjectToString(be.getBook_ebook_dollar_price()), 0);
                EBookTool.createBaseCell(wb, row, 3, "5", 0);
                EBookTool.createBaseCell(wb, row, 4, "", 0);
                EBookTool.createBaseCell(wb, row, 5, StringUtil.ObjectToString(be.getBook_isbn()), 0);
                EBookTool.createBaseCell(wb, row, 6, getEnglishLanByBookLan(be.getBook_language()), 0);
                EBookTool.createBaseCell(wb, row, 7, StringUtil.ObjectToString(be.getBook_name_cn()), 0);
                EBookTool.createBaseCell(wb, row, 8, StringUtil.ObjectToString(be.getBook_name_english()), 0);
                EBookTool.createBaseCell(wb, row, 9, StringUtil.ObjectToString(be.getBook_name_xi()), 0);
                EBookTool.createBaseCell(wb, row, 10, StringUtil.ObjectToString(be.getBook_name_e()), 1);
                EBookTool.createBaseCell(wb, row, 11, StringUtil.ObjectToString(be.getBook_author()), 0);
                EBookTool.createBaseCell(wb, row, 12, StringUtil.ObjectToString(be.getBook_author_english()), 0);
                EBookTool.createBaseCell(wb, row, 13, StringUtil.ObjectToString(be.getBook_author_xi()), 0);
                EBookTool.createBaseCell(wb, row, 14, StringUtil.ObjectToString(be.getBook_author_e()), 1);
                EBookTool.createBaseCell(wb, row, 15, StringUtil.ObjectToString(be.getBook_content_intr_cn()), 0);
                EBookTool.createBaseCell(wb, row, 16, StringUtil.ObjectToString(be.getBook_content_intr_english()), 0);
                EBookTool.createBaseCell(wb, row, 17, StringUtil.ObjectToString(be.getBook_content_intr_xi()), 0);
                EBookTool.createBaseCell(wb, row, 18, StringUtil.ObjectToString(be.getBook_content_intr_e()), 1);
                EBookTool.createBaseCell(wb, row, 19, StringUtil.ObjectToString(be.getBook_keyword_cn()), 0);
                EBookTool.createBaseCell(wb, row, 20, StringUtil.ObjectToString(be.getBook_keyword_english()), 0);
                EBookTool.createBaseCell(wb, row, 21, StringUtil.ObjectToString(be.getBook_keyword_xi()), 0);
                EBookTool.createBaseCell(wb, row, 22, StringUtil.ObjectToString(be.getBook_keyword_e()), 1);
                rowNum++;
            }
            wb.write(out);
            out.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getEnglishLanByBookLan(String bookLan) {
        if("001--英文".equals(bookLan)) {
            return "English";
        } else if("002--西文".equals(bookLan)) {
            return "Spanish";
        } else if("003--中文".equals(bookLan)) {
            return "Chinese";
        } else if("004--法文".equals(bookLan)) {
            return "French";
        } else if("005--德语".equals(bookLan)) {
            return "German";
        } else if("006--阿语".equals(bookLan)) {
            return "Arabic";
        } else if("007--俄语".equals(bookLan)) {
            return "Russian";
        } else if("008--土文".equals(bookLan)) {
            return "Turkish";
        } else if("009--日语".equals(bookLan)) {
            return "Japanese";
        } else if("010--韩语".equals(bookLan)) {
            return "Korean";
        } else if("011--意大利语".equals(bookLan)) {
            return "Italian";
        } else if("012--印尼文".equals(bookLan)) {
            return "Indonesian";
        } else if("013--哈萨克斯坦文".equals(bookLan)) {
            return "Kazakh";
        } else if("014--蒙文".equals(bookLan)) {
            return "Mongolian";
        } else if("015--藏文".equals(bookLan)) {
            return "Tibetan";
        } else if("016--波斯文".equals(bookLan)) {
            return "Persian";
        } else if("017--柯尔克孜文".equals(bookLan)) {
            return "Kirghiz";
        } else  {
            return "";
        }
    }
}
