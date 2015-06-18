package com.wz.service.eBookFactory;

import com.wz.common.ConfigInfo;
import com.wz.service.eBookFactory.impl.*;

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
		} else if(ConfigInfo.EBOOK_OS_CHINABOOKSTORE.equals(eBookOs)){
			ebook = new ChinaBookStoreEBookImpl();
		} else if(ConfigInfo.EBOOK_OS_HANBANHUATU.equals(eBookOs)) {
			ebook = new HanBanHuaTuEBookImpl();
		} else if(ConfigInfo.EBOOK_OS_IBOOKS.equals(eBookOs)) {
			ebook = new IBooksEBookImpl();
		} else if(ConfigInfo.EBOOK_OS_SOHUCHANGYOU.equals(eBookOs)) {
			ebook = new SoHuChangYouEBookImpl();
		} else if(ConfigInfo.EBOOK_OS_ZHONGTUYIYUE.equals(eBookOs)) {
			ebook = new ZhongTuYiYueTongEBookImpl();
		} else {
			System.out.println("其他平台："+eBookOs);
		}
		return ebook;
	}

	
}
