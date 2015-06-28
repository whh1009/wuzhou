package com.wz.service;

import com.wz.dao.BookExtendDao;
import com.wz.entity.BookExtendEntity;

/**
 * Created by Administrator on 2015/6/29.
 */
public class BookExtendService {
    private BookExtendDao bookExtendDao;
    public BookExtendService() {
        bookExtendDao = new BookExtendDao();
    }

    /**
     * 根据图书ID 查找图书 存在返回true，否则返回false；
     *
     * @param bookId
     * @return
     * @throws Exception
     */
//    public boolean findBookExtendByBookId(Integer bookId) throws Exception{
//        if(bookExtendDao.getBookEntityById(bookId)==null) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    /**
     * 根据图书ID 查找图书
     *
     * @param bookId
     * @return
     * @throws Exception
     */
    public BookExtendEntity findBookExtendByBookId(Integer bookId) throws Exception {
        return bookExtendDao.getBookEntityById(bookId);
    }

    /**
     * 更新
     * @param bee
     * @return
     * @throws Exception
     */
    public boolean updateBookExtend(BookExtendEntity bee) throws Exception {
        int updateCount = bookExtendDao.updateBook(bee);
        if(updateCount==1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean saveBookExtend(BookExtendEntity bee) throws Exception {
        if(bookExtendDao.saveBook(bee)==1) {
            return true;
        } else {
            return false;
        }
    }
}
