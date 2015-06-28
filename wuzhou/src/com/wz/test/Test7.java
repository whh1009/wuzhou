package com.wz.test;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by wanghonghui on 2015/4/16.
 */
public class Test7 {
    public static void main(String [] args) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        boolean a = false;
        boolean b = false;
        boolean c = false;
        boolean d = false;

        System.out.println(a&b&c&d);
    }
}
