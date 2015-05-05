package com.wz.test;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by wanghonghui on 2015/4/16.
 */
public class Test7 {
    public static void main(String [] args) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        Integer a = new Integer(9);
        Integer b = new Integer(9);
//        a = 9;
//        b = 10;
        System.out.println(a.equals(b));
        System.out.println(a==b);
    }
}
