package com.wz.service.eBookFactory;

import java.util.List;

import com.wz.common.ConfigInfo;
import com.wz.entity.BookEntity;
import com.wz.entity.UserEntity;
import com.wz.service.eBookFactory.impl.AmazonCnEBookImpl;
import com.wz.service.eBookFactory.impl.AmazonUsEBookImpl;
import com.wz.service.eBookFactory.impl.OverDriveEBookImpl;
import com.wz.service.eBookFactory.impl.ThatsBookEBookImpl;

/**
 * 电子书工厂
 * @author wanghonghui
 *
 */
public class EBookFormatFactory {
	private EBookFormatFactory(){ //单例模式
		
	}
	
	public static EBookFormat getInstence(String eBookOs) {
		EBookFormat ebook = null;
		if(ConfigInfo.EBOOK_OS_AMAZON_CN.equals(eBookOs)) {
			ebook = new AmazonCnEBookImpl();
		} else if(ConfigInfo.EBOOK_OS_AMAZON_US.equals(eBookOs)) {
			ebook = new AmazonUsEBookImpl();
		} else if(ConfigInfo.EBOOK_OS_OVERDRIVE.equals(eBookOs)) {
			ebook = new OverDriveEBookImpl();
		} else if(ConfigInfo.EBOOK_OS_THATSBOOK.equals(eBookOs)) {
			ebook = new ThatsBookEBookImpl();
		} else {
			
		}
		return ebook;
	}

	
}
