package com.wz.test;

import com.wz.entity.BookEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by wanghonghui on 2015/4/16.
 */
public class Test7 {
    public static void main(String [] args) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        BookEntity be = new BookEntity();
        String columnName="book_id"; //class java.lang.Integer
        columnName = "book_ebook_price"; //class java.lang.Float
//        columnName = "book_serial_number";//class java.lang.String
        BeanUtils.setProperty(be, columnName, "5.99");
        System.out.println(be.getClass().getField(columnName).getType().toString());
        System.out.println(be.getClass().getField(columnName).getType() + "==" +be.getBook_ebook_price());
    }
}
