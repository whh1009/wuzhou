package com.wz.common;

public class ConfigInfo {
	/**
	 * 每页显示记录条数
	 */
	public final static int PAGE_ROW_COUNT = 15;
	
	/**
	 * 书目信息默认显示字段
	 */
	public final static String DEFAULT_SHOW_COLUMN = 
			"book_serial_number," +
			"book_name_cn," +
			"book_author," +
			"book_translator," +
			"book_editor," +
			"book_language," +
			"book_paper_price," +
			"book_publish_time";
	
	/**
	 * 书目信息默认导出字段
	 */
	public final static String DEFAULT_EXPORT_COLUMN =
			"book_serial_number," +
			"book_name_cn," +
			"book_author," +
			"book_editor," +
			"book_translator," +
			"book_category1," +
			"book_content_intr_cn," +
			"book_language," +
			"book_paper_price," +
			"book_publish_time";
	
	/**
	 * 默认检索条件
	 */
	public final static String DEFAULT_SEARCH_COLUMN =
			"book_serial_number,"+
			"book_name_cn,"+			
			"book_editor,"+
			"book_author,"+
			"book_translator,"+
			"book_paper_price,"+
			"book_publish_time";
	
	/**
	 * 版权到期提醒
	 * 默认90天提醒
	 */
	public final static int COPYRIGHT_EXPIRES_REMIND_DAY = 90;
	
	/**
	 * 角色表单控制隐藏项
	 */
	public final static String ROLE_FROM_CONFIG = 
				"<config>" +
					//角色为7(责编)的，不显示name后的表单内容
					"<item roleId=\"7\" name=\"bookEntity.book_paper_dollar_price," +
					"bookEntity.book_ebook_price," +
					"bookEntity.book_ebook_dollar_price," +
					"bookEntity.book_name_fa,"+"bookEntity.book_name_xi,"+"bookEntity.book_name_e,"+
					"bookEntity.book_author_fa,"+"bookEntity.book_author_xi,"+"bookEntity.book_author_e,"+
					"bookEntity.book_editor_fa,"+"bookEntity.book_editor_xi,"+"bookEntity.book_editor_e,"+
					"bookEntity.book_translator_fa,"+"bookEntity.book_translator_xi,"+"bookEntity.book_translator_e,"+
					"bookEntity.book_series_fa,"+"bookEntity.book_series_xi,"+"bookEntity.book_series_e,"+
					"bookEntity.book_keyword_fa,"+"bookEntity.book_keyword_xi,"+"bookEntity.book_keyword_e,"+
					"bookEntity.book_content_intr_fa,"+"bookEntity.book_content_intr_xi,"+"bookEntity.book_content_intr_e,"+
					"bookEntity.book_author_intr_fa,"+"bookEntity.book_author_intr_xi,"+"bookEntity.book_author_intr_e,"+
					"bookEntity.book_editor_recommend_fa,"+"bookEntity.book_editor_recommend_xi,"+"bookEntity.book_editor_recommend_e,"+
					"bookEntity.book_e_xml,"+"bookEntity.book_e_epub,"+"bookEntity.book_e_mobi,"+"bookEntity.book_e_pdf,"+"bookEntity.book_e_html,"+
					
					"printToDocx\" />" +
				"</config>";
	
	/**
	 * 角色配置word不打印
	 */
	public final static String ROLE_WORD_CONFIG = "";
	
	/**
	 * FTP根目录
	 */
	public final static String FTP_ROOT = "e:\\ftp";
	
	/**
	 * 公共文件夹目录
	 * 用于存放已经转换完数据的目录
	 */
	public final static String COMMON_ROOT = "e:\\平台元数据";
	
	/**
	 * 亚马逊（中国）
	 */
	public final static String EBOOK_OS_AMAZON_CN = "amazoncn";
	/**
	 * 亚马逊（中国）元数据路径
	 */
	public final static String AMAZON_CN_EXCEL_PATH = System.getProperty("user.dir")+"/";;
	
	/**
	 * 亚马逊（美国）
	 */
	public final static String EBOOK_OS_AMAZON_US = "amazonus";
	/**
	 * 亚马逊（美国）元数据路径
	 */
	public final static String AMAZON_US_EXCEL_PATH = "";
	
	/**
	 * Over Drive
	 */
	public final static String EBOOK_OS_OVERDRIVE = "overdrive";
	/**
	 * Over Drive元数据路径
	 */
	public final static String OVERDRIVE_EXCEL_PATH = "";
	
	/**
	 * That's Books on China
	 */
	public final static String EBOOK_OS_THATSBOOK = "thatsbook";
	/**
	 * That's Books on China元数据路径
	 */
	public final static String THATSBOOK_EXCEL_PATH = "";

	/**
	 * China Book Store
	 */
	public final static String EBOOK_OS_CHINABOOKSTORE = "chinabookstore";

	/**
	 * 汉办华图
	 */
	public final static String EBOOK_OS_HANBANHUATU = "hanbanhuatu";

	/**
	 * iBooks
	 */
	public final static String EBOOK_OS_IBOOKS = "ibooks";

	/**
	 * 搜狐畅游
	 */
	public final static String EBOOK_OS_SOHUCHANGYOU = "sohuchangyou";

	/**
	 * 中图易越
	 */
	public final static String EBOOK_OS_ZHONGTUYIYUE = "zhongtuyiyue";


	/**
	 * 把现有的epub、pdf等电子档在服务器上的存放路径
	 */
	public final static String EBOOK_ROOT_PATH = "e:\\电子书成品";

	/**
	 * 样章归档目录
	 */
	public final static String EBOOK_YZ_PATH = "e:\\样章成品";

	/**
	 * 限制内文文件夹大小50KB
	 */
	public final static long LIMIT_NEIWEN_DIRECTORY_SIZE = 50*1024;

	/**
	 * 限制封面文件夹大小50KB
	 */
	public final static long LIMIT_COVER_DIRECTORY_SIZE = 50*1024;
	/**
	 * 限制分层PDF文件夹大小100KB
	 */
	public final static long LIMIT_FENCENG_PDF_DIRECTORY_SIZE = 100*1024;
	/**
	 * 限制合同文件夹大小1KB
	 */
	public final static long LIMIT_CONTRACT_DIRECTORY_SIZE = 1*1024;

	/**
	 * 五洲 限制必须输入的字段
	 */
	public final static String LIMIT_BOOKINFO_WUZHOU = "book_isbn,book_language,book_name_cn,book_publish_time,book_cooperate_press,book_clcc,book_editor,book_name_english,book_author,book_author_english,book_translator,book_translator_english,book_series_cn,book_series_english,book_category1,book_publish_count,book_copyright_word_count,book_copyright_expires,book_print_count,book_size,book_kaiben,book_heigh,book_paper_price,book_keyword_cn,book_keyword_english,book_content_intr_cn,book_content_intr_english,book_author_intr_cn,book_author_intr_english,book_editor_recommend_cn,book_editor_recommend_english,book_cover_paper,book_image_paper,book_neiwen_paper,book_cover_publish_color,book_image_publish_color,book_neiwen_publish_color,book_zhuangzhen_class,book_zhuangzhen_type,book_neiwen_page_count,book_image_page_count,book_publish_page_count,book_design_cover_company,book_design_style_company,book_news_publish_count,book_news_distribute_count,book_news_count,book_news_shangjiao_count,book_publish_soft,book_paper_neiwen_style_file,book_paper_cover_style_file,book_paper_publish_pdf,book_contract";

	/**
	 * 国内 限制必须输入的字段
	 */
	public final static String LIMIT_BOOKINFO_GUONEI = "book_isbn,book_language,book_name_cn,book_publish_time,book_cooperate_press,book_clcc,book_editor,book_name_english,book_author,book_author_english,book_name_fa,book_name_xi,book_name_e,book_author_fa,book_author_xi,book_author_e,book_category1,book_ebook_price,book_ebook_dollar_price,book_content_intr_cn,book_content_intr_english,book_author_intr_cn,book_author_intr_english,book_editor_recommend_cn,book_editor_recommend_english,book_content_intr_fa,book_content_intr_xi,book_content_intr_e,book_author_intr_fa,book_author_intr_xi,book_author_intr_e,book_editor_recommend_fa,book_editor_recommend_xi,book_editor_recommend_e";

	/**
	 * 国际 限制必须输入的字段
	 */
	public final static String LIMIT_BOOKINFO_GUOJI = "book_isbn,book_language,book_name_cn,book_publish_time,book_cooperate_press,book_editor,book_author,book_name_foreign,book_author_foreign,book_category1,book_ebook_dollar_price,book_content_intr_cn,book_author_intr_cn,book_editor_recommend_cn,book_content_intr_foreign,book_author_intr_foreign,book_editor_recommend_foreign";

	/**
	 * 合作社 限制必须输入的字段
	 */
	public final static String LIMIT_BOOKINFO_HEZUOSHE = "";
	
}
