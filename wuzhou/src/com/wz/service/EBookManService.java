package com.wz.service;

import com.wz.common.ColumnMap;
import com.wz.common.ConfigInfo;
import com.wz.dao.BookDao;
import com.wz.dao.EBookDao;
import com.wz.dao.UserDao;
import com.wz.entity.*;
import com.wz.service.eBookFactory.EBookFormat;
import com.wz.service.eBookFactory.EBookFormatFactory;
import com.wz.util.FileUtil;
import com.wz.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EBookManService {
    public static Logger log = Logger.getLogger("wuzhou");
    private EBookDao eBookDao;
    private BookDao bookDao;
    private UserDao userDao;

    public EBookManService() {
        eBookDao = new EBookDao();
        bookDao = new BookDao();
        userDao = new UserDao();
    }

    /**
     * 平台列表
     *
     * @return
     * @throws Exception
     */
    public List<BookOnlineOSEntity> getBookOnlineOS() throws Exception {
        return eBookDao.getBookOnlineOS();
    }

    /**
     * 添加平台
     *
     * @param bookOnlineOSEntity
     * @return
     * @throws Exception
     */
    public boolean addEBookOS(BookOnlineOSEntity bookOnlineOSEntity) throws Exception {
        return eBookDao.addEBookOS(bookOnlineOSEntity);
    }

    /**
     * 删除平台
     *
     * @param osId
     * @return
     * @throws Exception
     */
    public boolean deleteEBookOS(Integer osId) throws Exception {
        return eBookDao.deleteEBookOS(osId);
    }

    /**
     * 修改
     *
     * @param bookOnlineOSEntity
     * @return
     * @throws Exception
     */
    public boolean updateEBookOS(BookOnlineOSEntity bookOnlineOSEntity) throws Exception {
        return eBookDao.updateEBookOS(bookOnlineOSEntity);
    }

    public boolean formatEBook(List<BookEntity> bookList, List<UserEntity> userList, String excelPath) throws Exception {
        if (bookList == null || bookList.size() == 0) return false;
        String time = StringUtil.dateToString("yyyyMMddhhmmss"); //都生成到当前时间的文件夹中
        String overDrivePath = ConfigInfo.COMMON_ROOT + "\\" + time + "\\OverDrive";
        FileUtil.createDirs(overDrivePath, true);
        OutputStream overDriveStream = new FileOutputStream(new File(overDrivePath + "\\Over Drive.xlsx"));

        String thatsBookPath = ConfigInfo.COMMON_ROOT + "\\" + time + "\\That's Books on China";
        FileUtil.createDirs(thatsBookPath, true);
        OutputStream thatBookStream = new FileOutputStream(new File(thatsBookPath + "\\Thats Books on china.xlsx"));

        String amazonCnPath = ConfigInfo.COMMON_ROOT + "\\" + time + "\\亚马逊（中国）";
        FileUtil.createDirs(amazonCnPath, true);
        OutputStream amazonCnStream = new FileOutputStream(new File(amazonCnPath + "\\亚马逊（中国）.xlsx"));

        String amazonUsPath = ConfigInfo.COMMON_ROOT + "\\" + time + "\\亚马逊（美国）";
        FileUtil.createDirs(amazonUsPath, true);
        OutputStream amazonUsStream = new FileOutputStream(new File(amazonUsPath + "\\亚马逊（美国）.xlsx"));

        EBookFormat amazoncn = EBookFormatFactory.getInstence(ConfigInfo.EBOOK_OS_AMAZON_CN);
        amazoncn.renameEBook(bookList, userList, amazonCnPath, excelPath + "\\amazoncn.xlsx");
        amazoncn.createExcel(amazonCnStream);

        EBookFormat amanzonus = EBookFormatFactory.getInstence(ConfigInfo.EBOOK_OS_AMAZON_US);
        amanzonus.renameEBook(bookList, userList, amazonUsPath, excelPath + "\\amazonus.xlsx");
        amanzonus.createExcel(amazonUsStream);

        EBookFormat onedrive = EBookFormatFactory.getInstence(ConfigInfo.EBOOK_OS_OVERDRIVE);
        onedrive.renameEBook(bookList, userList, overDrivePath, excelPath + "\\overdrive.xlsx");
        onedrive.createExcel(overDriveStream);

        EBookFormat thatsbook = EBookFormatFactory.getInstence(ConfigInfo.EBOOK_OS_THATSBOOK);
        thatsbook.renameEBook(bookList, userList, thatsBookPath, excelPath + "\\thatsbook.xlsx");
        thatsbook.createExcel(thatBookStream);
        return true;
    }

    /**
     * 把在线平台列表转为xml
     *
     * @return
     * @throws Exception
     */
    public String getOsXml() throws Exception {
        List<BookOnlineOSEntity> list = getBookOnlineOS();
        if (list == null || list.isEmpty()) return "";
        String out = "<os>";
        for (BookOnlineOSEntity os : list) {
            out += "<item osId='" + os.getOs_id() + "' osName='" + os.getOs_name() + "' />";
        }
        return out += "</os>";
    }

    public List<BookOnlineEntity> getOnlineOsList(List<BookEntity> bookList) throws Exception {
        if (bookList == null || bookList.isEmpty()) return null;
        String bookIds = "";
        for (BookEntity be : bookList) {
            bookIds += be.getBook_id() + ",";
        }
        bookIds = StringUtil.ignoreComma(bookIds);
        return eBookDao.getBookOnline(bookIds);
    }

    /**
     * 根据bookId获取平台列表xml
     *
     * @param bookId
     * @return
     * @throws Exception
     */
    public String getOnlineOsByBookId(int bookId) throws Exception {
        List<BookOnlineEntity> list = eBookDao.getBookOnlineByBookId(bookId);
        if (list == null || list.isEmpty()) return "";
        String out = "<bookOsMap>";
        for (BookOnlineEntity bo : list) {
            out += "<item onlineId='" + bo.getOnline_id() + "' osId='" + bo.getOs_id() + "' isOnline='" + bo.getIs_online() + "' />";
        }
        out += "</bookOsMap>";
        return out;
    }

    public int updateBookOnlineByXml(String markStr) {
        try {
            Document doc = DocumentHelper.parseText(markStr);
            List<Element> itemList = doc.selectNodes("//item");
            if (itemList == null || itemList.isEmpty()) {
                return 0;
            } else {
                int updateCount = 0;
                for (Element ele : itemList) {
                    int bookId = StringUtil.StringToInt(ele.attributeValue("bookId"));
                    int osId = StringUtil.StringToInt(ele.attributeValue("osId"));
                    int isOnline = bool2int(ele.attributeValue("isOnline"));
                    BookOnlineEntity bo = eBookDao.getBookOnlineByBookIdAndOsId(bookId, osId);
                    String sql = "";
                    if (bo == null) {
                        if (isOnline == 1) {
                            sql = "insert into wz_book_online (book_id, os_id, is_online) values (" + bookId + " , " + osId + ", 1)";
                        } else {

                        }
                    } else {
                        if (isOnline == 1) {

                        } else {
                            sql = "delete from wz_book_online where book_id = " + bookId + " and os_id = " + osId + "";
                        }
                    }
                    if (!"".equals(sql)) {
                        updateCount += eBookDao.updateBookOnline(sql);
                    } else {
                        updateCount += 0;
                    }
                }
                if (updateCount == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private int bool2int(String str) {
        //空返回0
        if (StringUtil.ObjectToString(str).length() == 0) return 0;
        if ("true".equals(str)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 获取服务器上存量资源的文件列表
     *
     * @return
     */
    public String getEBookFileList() {
//		String epubFold = ConfigInfo.EBOOK_ROOT_PATH+"\\EPUB";
//		String mobiFold = ConfigInfo.EBOOK_ROOT_PATH+"\\MOBI";
//		String pdfFold = ConfigInfo.EBOOK_ROOT_PATH+"\\PDF";
//		String jpgFold = ConfigInfo.EBOOK_ROOT_PATH+"\\封面";
        return "";
    }

    /**
     * 加载元数据，获取不重复的书目
     *
     * @return
     * @throws java.io.IOException
     */
    public List<YuanDataEntity> loadYuanDataExcel(String excelPath) throws IOException {
        List<YuanDataEntity> list = new ArrayList<YuanDataEntity>();
        File excelFile = new File(excelPath);
        if (!excelFile.exists()) {
            System.out.println(excelPath + " 不存在");
            return null;
        } else {
            InputStream in = new FileInputStream(excelFile);
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            YuanDataEntity yuan = null;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { //从第二行开始，忽略第一行的标题"isbn 书名等信息"
                XSSFRow row = sheet.getRow(i);
                yuan = new YuanDataEntity();
                yuan.setIsbn(getCellValue(row.getCell(0)).trim());
                yuan.setName(getCellValue(row.getCell(1)).trim());
                list.add(yuan);
            }
            return removeDuplicate(list);
        }
    }

    /**
     * 移动电子书，电子书归档
     *
     * @param list
     * @return
     * @throws Exception
     */
    public String moveEBooks(List<YuanDataEntity> list) throws Exception {
        String out = "";
        File[] files = new File(ConfigInfo.EBOOK_ROOT_PATH).listFiles();
        for (File file : files) {
            boolean flag = false;
            String fullName = file.getName().toLowerCase().trim();
            if (!fullName.contains(".")) {
                out += "<p style='color:red'>文件名不对：" + fullName + "</p>";
            } else {
                String name = fullName.substring(0, fullName.lastIndexOf("."));
                String extension = fullName.substring(fullName.lastIndexOf(".") + 1);
                if ("epub".equals(extension) || "mobi".equals(extension) || "pdf".equals(extension) || "jpg".equals(extension)) {
                    for (YuanDataEntity yuan : list) {
                        if (name.equals(yuan.getName())) {
                            flag = true;
                            String isbn = yuan.getIsbn();
                            if (isbn.contains("_")) {
                                isbn = isbn.split("_")[1] + isbn.split("_")[0];
                            }
                            BookEntity be = bookDao.getBookPathByIsbn(isbn);
                            if (be != null) {
                                String baseBookPath = "";
                                if (be.getUser_id() == 1) { //存量资源
                                    baseBookPath = ConfigInfo.FTP_ROOT + "\\201409之前书目\\" + be.getBook_serial_number();
                                } else {
                                    baseBookPath = ConfigInfo.FTP_ROOT + "\\" + userDao.getUserEntityByUserId(be.getUser_id()).getUser_name() + "\\" + be.getBook_serial_number();
                                }
                                if ("epub".equals(extension)) {
                                    new File(baseBookPath + "\\EPUB").mkdirs();
                                    FileUtil.copyFile(file, new File(baseBookPath + "\\EPUB\\" + fullName), true);
                                } else if ("mobi".equals(extension)) {
                                    new File(baseBookPath + "\\MOBI").mkdirs();
                                    FileUtil.copyFile(file, new File(baseBookPath + "\\MOBI\\" + fullName), true);
                                } else if ("pdf".equals(extension)) {
                                    new File(baseBookPath + "\\阅读PDF").mkdirs();
                                    FileUtil.copyFile(file, new File(baseBookPath + "\\阅读PDF\\" + fullName), true);
                                } else if ("jpg".equals(extension)) {
                                    new File(baseBookPath + "\\封面").mkdirs();
                                    FileUtil.copyFile(file, new File(baseBookPath + "\\封面\\" + fullName), true);
                                } else {

                                }
                                out += "<p>移动：" + fullName + "</p>";
                                break;
                            } else {
                                out += "<p style='color:red;'>数据库中未找到或有多本相同ISBN号的书目：" + fullName + "</p>";
                                flag = true;
                            }
                        } else {
                            flag = false;
                        }
                    }
                    if (!flag) {
                        out += "<p style='color:red;'>EXCEL中未找到或找到多个：" + fullName + "</p>";
                    }
                }
            }

        }
        return out;
    }

    /**
     * 移动样章，电子书封面归档
     *
     * @param excelFile
     * @return
     * @throws Exception
     */
    public String moveYZEBooks(File excelFile) throws Exception {
        File[] files = excelFile.getParentFile().listFiles();
        StringBuffer sb = new StringBuffer();
        InputStream in = new FileInputStream(excelFile);
        XSSFWorkbook wb = new XSSFWorkbook(in);
        XSSFSheet sheet = wb.getSheet("Sheet1");//固定Sheet名
        for (int i = 1; i <= sheet.getLastRowNum(); i++) { //从第二行开始，忽略第一行的标题"isbn 书名等信息"
            XSSFRow row = sheet.getRow(i);
            String bianhao = getCellValue(row.getCell(0)).trim();
            String name = getCellValue(row.getCell(2)).trim();
            List<BookEntity> list = bookDao.getBookListByHql("from BookEntity where book_serial_number like '%\\_" + bianhao.replace("_", "\\_") + "\\_%'");
            if (list == null || list.isEmpty()) {
                sb.append("<p class='text-danger'>").append(bianhao).append("\t").append(name).append("采集系统中未找到</p>");
            } else {
                if (list.size() > 1) {
                    sb.append("<p class='text-danger'>").append(bianhao).append("\t").append(name).append("采集系统中有多本</p>");
                } else {
                    BookEntity be = list.get(0);
                    if (be == null) {
                        sb.append("<p class='text-danger'>").append(bianhao).append("\t").append(name).append("获取图书资源失败</p>");
                    } else {
                        boolean flag = false;
                        for (File ff : files) {
                            String fullName = ff.getName().toLowerCase().trim();
                            String fileName = fullName.substring(0, fullName.lastIndexOf("."));
                            String extension = fullName.substring(fullName.lastIndexOf(".") + 1);
                            if (name.equals(fileName)) {
                                String baseBookPath = "";
                                if (be.getUser_id() == 1) { //存量资源
                                    baseBookPath = ConfigInfo.FTP_ROOT + "\\201409之前书目\\" + be.getBook_serial_number();
                                } else {
                                    baseBookPath = ConfigInfo.FTP_ROOT + "\\" + userDao.getUserEntityByUserId(be.getUser_id()).getUser_name() + "\\" + be.getBook_serial_number();
                                }
                                if ("jpg".equals(extension)) {
                                    copyFile(ff, baseBookPath + "\\电子书封面", fullName);
                                } else {
                                    copyFile(ff, baseBookPath + "\\样章", fullName);
                                }
                                sb.append("<p class='text-info'>").append(bianhao).append("\t").append(fullName).append("已经复制</p>");
                                flag = true;
                            }
                        }
                        if (!flag) {
                            sb.append("<p style='color:red'>").append(name).append("\t").append(excelFile.getParent()).append("文件夹中没有找到</p>");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public void copyFile(File srcFile, String destPath, String destFileName) throws Exception {
        new File(destPath).mkdirs();
        FileUtil.copyFile(srcFile, new File(destPath + "\\" + destFileName), true);
    }

    /**
     * 去重，本身也删除
     * aa,bb,aa,cc====bb,cc
     *
     * @param list
     * @return
     */
    public List<YuanDataEntity> removeDuplicate(List<YuanDataEntity> list) {
        if (list == null) return null;
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i).getName();
            boolean flag = false;
            for (int j = i + 1; j < list.size(); j++) {
                String name2 = list.get(j).getName();
                if (name.equals(name2)) {
                    list.remove(list.get(j));
                    flag = true;
                }
            }
            if (flag) {
                list.remove(i);
                removeDuplicate(list);
            }

        }
        return list;
    }

    /**
     * 获取单元格文本
     *
     * @param cell
     * @return
     */
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
                try {
//				cellValue = cell.getStringCellValue();
                    cellValue = String.valueOf(cell.getNumericCellValue());
                } catch (Exception ex) {
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
                try {
//				cellValue = cell.getStringCellValue();
                    cellValue = String.valueOf(cell.getNumericCellValue());
                } catch (Exception ex) {
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

    /**
     * 根据excel更新图书价格
     *
     * @param excelPath
     * @return
     * @throws Exception
     */
    public String updateBookPrice(String excelPath) throws Exception {
        File excelFile = new File(excelPath);
        if (!excelFile.exists()) return "<p style='color:red'>excel路径有误：" + excelPath + "</p>";
        String out = "";
        InputStream in = new FileInputStream(excelFile);
        XSSFWorkbook wb = new XSSFWorkbook(in);
        XSSFSheet sheet = wb.getSheet("Sheet1");
        System.out.println(sheet.getLastRowNum());
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            String isbn = getCellValue(row.getCell(1)).replace("-", "");
            String wenban = getCellValue(row.getCell(2)); //文版
            if ("".equals(wenban)) {
                if (getCellValue(row.getCell(3)).contains("对照）")) {
                    wenban = "500";
                } else {
                    wenban = "没有匹配的文版";
                    continue;
                }
            }
            BookEntity be = bookDao.getBookEntityBySql("from BookEntity where book_serial_number like '%\\_" + isbn + "_" + wenban + "\\_%'");
            String bookName = getCellValue(row.getCell(3));
            float bookRmbPrice = Float.parseFloat(getCellValue2(row.getCell(4)));
            float ebookRmbPrice = Float.parseFloat(getCellValue2(row.getCell(5)));
            float ebookUsPrice = Float.parseFloat(getCellValue2(row.getCell(6)));
            if (be == null) {
                out += "<p style='color:red'>数据库中未找到:\t" + isbn + "\t" + wenban + "\t" + bookName + "</p>";
            } else {
                be.setBook_paper_price(bookRmbPrice);
                be.setBook_ebook_price(ebookRmbPrice);
                be.setBook_ebook_dollar_price(ebookUsPrice);
                if (bookDao.updateBook(be) == 1) {
                    out += "<p>成功处理图书：\t" + isbn + "\t" + wenban + "\t" + bookName + "</p>";
                } else {
                    out += "<p style='color:red'>处理图书失败：\t" + isbn + "\t" + wenban + "\t" + bookName + "</p>";
                }
            }
        }
        System.out.println(out);
        return out;
    }


    public static void main(String[] args) throws IOException {
        EBookManService e = new EBookManService();
        String excelPath = "C:\\Users\\Administrator\\Desktop\\电子书价格.xlsx";
        InputStream in = new FileInputStream(new File(excelPath));
        XSSFWorkbook wb = new XSSFWorkbook(in);
        XSSFSheet sheet = wb.getSheet("Sheet1");
//		HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
//		sheet.setForceFormulaRecalculation(true);
        for (int i = 0; i <= sheet.getLastRowNum(); i++) { //从第二行开始，忽略第一行的"isbn 书名"
            XSSFRow row = sheet.getRow(i);
            System.out.println(e.getCellValue2(row.getCell(5)));
        }
    }

    /**
     * 更新电子书流程状态（注意1,2顺序不同）
     * 前段对应：
     * 0--待转码
     * 1--转码中
     * 2--转码完
     * 3--待上线
     * 4--已在线
     * 5--已下线
     * 数据库对应：
     * 0--转码中
     * 1--已上线
     * 2--转码完
     * 3--已停售
     * 4--待转码
     * 5--待上线
     *
     * @param result
     */
    public int updateOnlineStatus(String result) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(result);
        } catch (DocumentException ex) { //xml解析异常
            ex.printStackTrace();
            return -1;
        }
        Element root = doc.getRootElement();
        if (root == null) return -1;
        int bookId = StringUtil.StringToInt(root.attributeValue("bookId"));
        List<Element> itemList = doc.selectNodes("//item");
        if (itemList == null || itemList.isEmpty()) {
            if (eBookDao.deleteEBookOsByBookId(bookId)) {//删除成功
                return 1;
            } else { //删除失败
                return 2;
            }
        } else {
            for (Element ele : itemList) {
                int osId = StringUtil.StringToInt(ele.attributeValue("osId"));
                int status = StringUtil.StringToInt(ele.attributeValue("onlineStatus"));
                System.out.print("===" + status);
                if (status == 0) {
                    status = 4;
                } else if (status == 1) {
                    status = 0;
                } else if (status == 3) {
                    status = 5;
                } else if (status == 4) {
                    status = 1;
                } else if (status == 5) {
                    status = 3;
                }
                BookOnlineEntity boe = null;
                try {
                    boe = eBookDao.getBookOnlineByBookIdAndOsId(bookId, osId);
                    boolean flag = false;
                    if (boe == null) {
                        flag = false;
                        boe = new BookOnlineEntity();
                        boe.setBook_id(bookId);
                        boe.setOs_id(osId);
                    } else {
                        flag = true;
                    }

                    if (flag) { //数据库的平台表中存在这本书
                        if (status == 4) {
                            System.out.println("-----------------" + boe.getOnline_id());
                            eBookDao.deleteEBookOSByBookOnlineEntity(boe);
                        } else {
                            boe.setIs_online(status);
                            eBookDao.saveOrUpdateBookOnline(boe);
                        }
                    } else { //
                        if (status != 4) {
                            boe.setIs_online(status);
                            eBookDao.saveOrUpdateBookOnline(boe);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return 2;
                }
            }
            return 1;
        }
    }

    public List<BookOnlineEntity> getOnlineStatusBook(int osIdSel, int statusIdSel) {
        String hql = "";
        if (osIdSel == 0) { //检索全部平台
            hql = "from BookOnlineEntity where is_online = " + statusIdSel;
        } else {
            hql = "from BookOnlineEntity where os_id = " + osIdSel + " and is_online = " + statusIdSel;
        }
        return eBookDao.getOnlineBookByHql(hql);
    }

    //insert delayed ignore wz_book_online (book_id, os_id, is_online) values (1,1, 4) ON DUPLICATE KEY UPDATE  is_online=4
    public String getSheetsByOnlineStatus(String excelPath) throws Exception {
        String result = "";
        File excelFile = new File(excelPath);
        if (!excelFile.exists()) {
            result = "<p style='color:red'>excel路径有误：" + excelPath + "</p>";
        } else {
            try {
                InputStream in = new FileInputStream(excelFile);
                XSSFWorkbook wb = new XSSFWorkbook(in);
                for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                    XSSFSheet sheet = wb.getSheetAt(i);
                    String sheetName = sheet.getSheetName();
                    if (sheetName.contains("_")) {
                        result += "<div class='col-sm-4'><div class='checkbox'><label><input name='sheetName' type='checkbox' value='" + i + "'>" + sheetName + "</div></label></div>";
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        return result;
    }

    private int getBookIdByBookIsbn(String isbn) {
        List<BookEntity> list = bookDao.getBookListByHql("from BookEntity where book_serial_number like '%" + isbn.replace("_", "\\_") + "%'");
        if (list == null || list.isEmpty()) { //没有找到
            return -2;
        } else {
            if (list.size() > 1) { //找到多个
                return -1;
            } else {
                return list.get(0).getBook_id();
            }
        }
    }

    public String updateBookOnlineStatusBySheetIds(String excelPath, String sheetIds) throws Exception {
        StringBuffer sb = new StringBuffer();
        File excelFile = new File(excelPath);
        if (!excelFile.exists()) {
            sb.append("<p style='color:red'>excel路径有误：" + excelPath + "</p>");
        } else {
            try {
                InputStream in = new FileInputStream(excelFile);
                XSSFWorkbook wb = new XSSFWorkbook(in);
                for (int i = 0; i < sheetIds.split(",").length; i++) {
                    XSSFSheet sheet = wb.getSheetAt(Integer.parseInt(sheetIds.split(",")[i]));
                    int osId = StringUtil.StringToInt((sheet.getSheetName().split("_")[1]));
                    int rowCount = sheet.getLastRowNum();
                    if (rowCount > 1) {
                        for (int j = 1; j < rowCount; j++) {
                            XSSFRow row = sheet.getRow(j);
                            String book_isbn = getCellValue(row.getCell(0)).trim();
                            int is_online = StringUtil.StringToInt(getCellValue(row.getCell(6)));
                            if (!"".equals(book_isbn)) {
                                int bookId = getBookIdByBookIsbn(book_isbn);
                                if (bookId == -2) {
                                    sb.append("<p class='text-danger'>" + sheet.getSheetName() + "\t" + book_isbn + "没有找到</p>");
                                    log.debug(sheet.getSheetName() + "\t" + book_isbn + "没有找到</p>");
                                } else if (bookId == -1) {
                                    sb.append("<p class='text-danger'>" + sheet.getSheetName() + "\t" + book_isbn + "找到多个</p>");
                                    log.debug(sheet.getSheetName() + "\t" + book_isbn + "找到多个</p>");
                                } else {
                                    System.out.println(bookId + "==" + osId + "==" + book_isbn + "==" + is_online);
                                    //更新
                                    int flag = eBookDao.updateBookOnline("insert delayed ignore wz_book_online (book_id, os_id, is_online) values (" + bookId + "," + osId + ", " + is_online + ") ON DUPLICATE KEY UPDATE  is_online=" + is_online);
                                    if (flag == 1) {
                                        sb.append("<p class='text-muted'>").append(sheet.getSheetName()).append("\t").append(book_isbn).append("更新成功</p>");
                                        log.debug(sheet.getSheetName() + "\t" + book_isbn + "更新成功</p>");
                                    } else { //更新失败
                                        sb.append("<p class='text-primary'>").append(sheet.getSheetName()).append("\t").append(book_isbn).append("更新失败</p>");
                                        log.debug(sheet.getSheetName() + "\t" + book_isbn + "更新失败</p>");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        return sb.toString();
    }


    /**
     * 根据excel更新图书价格2
     * 电子书人民币和电子书美元
     *
     * @param excelPath
     * @return
     * @throws Exception
     */
    public String updateBookPrice2(String excelPath) throws Exception {
        File excelFile = new File(excelPath);
        if (!excelFile.exists()) return "<p style='color:red'>excel路径有误：" + excelPath + "</p>";
        String out = "";
        InputStream in = new FileInputStream(excelFile);
        XSSFWorkbook wb = new XSSFWorkbook(in);
        XSSFSheet sheet = wb.getSheet("books");
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            String isbn = getCellValue(row.getCell(0));
            BookEntity be = bookDao.getBookEntityBySql("from BookEntity where book_serial_number like '%\\_" + isbn + "\\_%'");
            String bookName = getCellValue(row.getCell(4));
            float ebookRmbPrice = Float.parseFloat(getCellValue2(row.getCell(5)));
            float ebookUsPrice = Float.parseFloat(getCellValue2(row.getCell(6)));
            if (be == null) {
                log.debug("找到多个或没找到：" + isbn + "\t" + bookName);
                out += "<p style='color:red'>数据库中有多个或未找到:\t" + isbn + "\t" + "\t" + bookName + "</p>";
            } else {
                be.setBook_ebook_price(ebookRmbPrice);
                be.setBook_ebook_dollar_price(ebookUsPrice);
                if (bookDao.updateBook(be) == 1) {
                    out += "<p>成功处理图书：\t" + isbn + "\t" + bookName + "</p>";
                    log.debug("成功处理图书：" + isbn + "\t" + bookName);
                } else {
                    out += "<p style='color:red'>处理图书失败：" + isbn + "\t" + bookName + "</p>";
                    log.debug("处理图书失败：" + isbn + "\t" + bookName);
                }
            }
        }
        return out;
    }

    /**
     * @param bookLan
     * @param osId
     * @param osStatus
     * @param exportExcelPath
     * @param exportColumn
     * @return
     * @throws Exception
     */
    public int exportEBookOnline(String bookLan, int osId, int osStatus, String exportExcelPath, String exportColumn) throws Exception {
        String hql = "from BookOnlineEntity where 1 = 1 ";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("wuzhou");
        Row row1 = sheet.createRow(0);
        String[] exportColumnArray = exportColumn.split(",");
        for (int i = 0; i < exportColumnArray.length; i++) {
            row1.createCell(i).setCellValue(ColumnMap.getBookTableCnByColumnName(exportColumnArray[i].trim()));
        }
        List<BookOnlineOSEntity> osList = eBookDao.getBookOnlineOS();
        if (osList == null || osList.isEmpty()) return -1;
        if (osId == 0) { //全部平台 //标题行
            int startColumn = exportColumnArray.length;
            for (BookOnlineOSEntity bos : osList) {
                row1.createCell(startColumn).setCellValue(bos.getOs_name());
                startColumn++;
            }
        } else {
            hql += " and os_id = " + osId;
            for (BookOnlineOSEntity bos : osList) {
                if (bos.getOs_id() == osId) {
                    row1.createCell(exportColumnArray.length).setCellValue(bos.getOs_name());
                }
            }
        }
        if (osStatus != -1) {
            hql += " and is_online = " + osStatus;
        }
        String conditions = " and 1 = 1";
        if (!"0".equals(bookLan)) {
            conditions = " and book_language = '" + bookLan + "'";
        }
        log.debug("hql:" + hql);
        List<BookOnlineEntity> bookOnlinelist = eBookDao.getOnlineBookByHql(hql);
        if (bookOnlinelist == null || bookOnlinelist.isEmpty()) return -1;
        List<BookEntity> bookList = bookDao.getBookListByHql("from BookEntity where book_del_flag=0 " + conditions);
        if (bookList == null || bookList.isEmpty()) return -1;
        int rowCountTemp = 1;
        Row row = null;
        for (int i = 0; i < bookList.size(); i++) {
            BookEntity be = bookList.get(i);
            boolean createRowFlag = false;
            if (osId == 0) {
                int startColumn = exportColumnArray.length;
                for (BookOnlineOSEntity bos : osList) {
                    boolean flag = false;
                    for (BookOnlineEntity boe : bookOnlinelist) {
                        if ("".equals(StringUtil.ObjectToString(boe.getOs_id()))) continue;

                        if (be.getBook_id().equals(boe.getBook_id()) && bos.getOs_id().equals(boe.getOs_id())) { //注意是Integer，不能用==
                            if (be.getBook_serial_number().equals("B_EP_7508510194_003_KodB0")) {
                                log.debug(rowCountTemp+"==" + StringUtil.ObjectToString(boe.getOs_id()) + "=="+bookOnlineStatusStr(boe.getIs_online())+"==");
                                log.debug("be.getBook_id():" + be.getBook_id() + "--boe.getBook_id():" + boe.getBook_id() + "--" + be.getBook_id().equals(boe.getBook_id()));
                                log.debug("bos.getOs_id():" + bos.getOs_id() + "--boe.getOs_id():" + boe.getOs_id() + "--" + bos.getOs_id().equals(boe.getOs_id()));
                            }
                            if (!createRowFlag) {
                                row = sheet.createRow(rowCountTemp);
                                rowCountTemp++;
                            }
                            createRowFlag = true;
                            row.createCell(startColumn).setCellValue(bookOnlineStatusStr(boe.getIs_online()));
                            flag = true;
                            break;
                        } else {
                            flag = false;
                        }
                    }
                    if (flag) {

                        for (int j = 0; j < exportColumnArray.length; j++) {
                            row.createCell(j).setCellValue(StringUtil.ObjectToString(BeanUtils.getProperty(be, exportColumnArray[j].trim())));
                        }

                    } else {
                        row.createCell(startColumn).setCellValue("");
                    }
                    startColumn++;
                }
            } else {
                for (BookOnlineEntity boe : bookOnlinelist) {
                    if (be.getBook_id().equals(boe.getBook_id()) && boe.getOs_id().equals(osId)) {
                        row = sheet.createRow(rowCountTemp);

                        for (int j = 0; j < exportColumnArray.length; j++) {
                            row.createCell(j).setCellValue(StringUtil.ObjectToString(BeanUtils.getProperty(be, exportColumnArray[j].trim())));
                        }
                        row.createCell(exportColumnArray.length).setCellValue(bookOnlineStatusStr(osId));
                        rowCountTemp++;
                        break;
                    }
                }
            }
        }
        FileOutputStream os = new FileOutputStream(exportExcelPath);
        wb.write(os);
        os.close();
        return 1;
    }

    /**
     * @param isOnline
     * @return
     */
    private String bookOnlineStatusStr(int isOnline) {
        String out = "";
        switch (isOnline) {
            case 0:
                out = "转码中";
                break;
            case 1:
                out = "已上线";
                break;
            case 2:
                out = "转码完";
                break;
            case 3:
                out = "已停售";
                break;
            case 4:
                out = "待转码";
                break;
            case 5:
                out = "待上线";
                break;
        }
        return out;
    }
}
