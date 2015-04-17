package com.wz.util;
import java.util.Comparator;

import com.wz.entity.MenuEntity;


/**
 * MenuEntity对象按照属性sort_id排序
 * 
 * @author 
 *
 */
public class MenuSort implements Comparator {
	public int compare(Object o1, Object o2) {
		MenuEntity menuEntity1 = (MenuEntity)o1;
		MenuEntity menuEntity2 = (MenuEntity)o2;
		//比较排序
		return (menuEntity1.getMenu_sort()).compareTo(menuEntity2.getMenu_sort());
	}

}
