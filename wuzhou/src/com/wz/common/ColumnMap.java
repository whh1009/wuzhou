package com.wz.common;

import com.wz.util.StringUtil;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * 数据库中、英文名映射
 */
public class ColumnMap {

	/**
	 * 默认显示的列数
	 * @return
	 */
	public static Map<String, String> initBookTableColumnMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("book_serial_number", "图书编号");
		map.put("book_name_cn", "书名（中文）");
		map.put("book_name_foreign", "书名（外文）");
		map.put("book_author", "著者");
		map.put("book_editor", "责编");
		map.put("book_translator", "译者");
		map.put("book_paper_price", "纸书定价");
		map.put("book_language", "文种");
		map.put("book_publish_time", "出版时间");
		return map;
	}

	/**
	 * 数据库中英文映射
	 * 用于配置显示、导出、检索字段
	 * @return
	 */
	public static Map<String, String> bookTableColumnMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("book_serial_number", "图书编号");
		map.put("book_name_cn", "书名（中文）");
		map.put("book_name_english", "书名（英文）");
		map.put("book_name_foreign", "书名（外文）");
		map.put("book_author", "著者（中文）");
		map.put("book_author_english", "著者（英文）");
		map.put("book_author_foreign", "著者（外文）");
		map.put("book_translator", "译者（中文）");
		map.put("book_translator_english", "译者（英文）");
		map.put("book_translator_foreign", "译者（外文）");
		map.put("book_editor", "责编");
//		map.put("book_editor_english", "责编（英文）");
//		map.put("book_editor_foreign", "责编（外文）");
		map.put("book_isbn", "ISBN/ISSN");
		map.put("book_clcc", "中图分类号");
		map.put("book_alcc", "美图分类号"); //0823
		map.put("book_category1", "建议分类1");
		map.put("book_category2", "建议分类2");
		map.put("book_category3", "建议分类3");
		map.put("book_series_cn", "丛书名（中文）");
		map.put("book_series_english", "丛书名（英文）");
		map.put("book_series_foreign", "丛书名（外文）");
		map.put("book_publish_count", "版次");
		map.put("book_print_count", "印次");
		map.put("book_copyright_word_count", "版权字数（千字）");
		map.put("book_size", "用纸尺寸");
		map.put("book_kaiben", "开本尺寸");
		map.put("book_heigh", "书脊厚度");
		map.put("book_language", "文种");
		map.put("book_bilingual", "双语对应");
		map.put("book_publish_time", "出版时间");
		map.put("book_copyright_expires", "版权到期");
		map.put("book_image_count", "插图数量");
		map.put("book_image_spread", "插图分布");
		map.put("book_table_count", "插表数量");
		map.put("book_disk_type", "配盘介质");
		map.put("book_disk_send", "光盘赠送方式");
		map.put("book_paper_price", "纸书定价（RMB）");
		map.put("book_paper_dollar_price", "纸书定价（USD）");
		map.put("book_ebook_price", "电子书定价（RMB）");
		map.put("book_ebook_dollar_price", "电子书定价（USD）");
		map.put("book_cover_paper", "封面用纸");
		map.put("book_cover_publish_color", "封面印刷颜色");
		map.put("book_image_paper", "插页用纸");
		map.put("book_image_publish_color", "插页印刷颜色");
		map.put("book_neiwen_paper", "内文用纸");
		map.put("book_neiwen_publish_color", "内文印刷颜色");
		map.put("book_zhuangzhen_class", "装帧类型");
		map.put("book_zhuangzhen_type", "装帧方式");
		map.put("book_end_fumo", "后工艺覆膜");
//		map.put("book_suoxianjiaoding", "锁线胶订");
//		map.put("book_end_face_handle", "后工艺表面处理");
		map.put("book_end_uv", "后工艺UV");
		map.put("book_end_lekou", "后工艺勒口");
		map.put("book_end_sufeng", "后工艺封装");
		map.put("book_end_other", "后工艺其他");
		map.put("book_design_cover_company", "封面设计公司");
		map.put("book_design_style_company", "版式制作公司");
		map.put("book_neiwen_page_count", "内文页数");
		map.put("book_image_page_count", "插页页数");
		map.put("book_publish_page_count", "总印张数");
		//map.put("book_evaluate_weight", "估计克重"); //0823
		map.put("book_weight", "成书克重");
		map.put("book_publish_explain", "印装说明");
		map.put("book_news_publish_count", "新闻办印数");
		map.put("book_news_distribute_count", "发行印数");
		map.put("book_news_count", "总印数");
		map.put("book_news_shangjiao_count", "上缴样书册数");
		map.put("book_keyword_cn", "中文关键词");
		map.put("book_keyword_english", "英文关键词");
		map.put("book_keyword_foreign", "外文关键词");
//		map.put("book_paper_neiwen_style_file", "纸质图书内文版式文件");
		map.put("book_paper_neiwen_style_file", "内文文件");
//		map.put("book_paper_cover_style_file", "纸质图书封面版式文件");
		map.put("book_paper_cover_style_file", "封面文件");
//		map.put("book_paper_font", "使用字体");
		map.put("book_paper_font", "字体文件");
//		map.put("book_paper_publish_pdf", "分层PDF");
		map.put("book_paper_publish_pdf", "分层PDF");
//		map.put("book_paper_word", "原word文件");
		map.put("book_paper_word", "word文档");
//		map.put("book_e_xml", "XML");
		map.put("book_e_xml", "XML文档");
//		map.put("book_e_epub", "EPUB");
		map.put("book_e_epub", "EPUB");
//		map.put("book_e_mobi", "MOBI");
		map.put("book_e_mobi", "MOBI");
//		map.put("book_e_pdf", "PDF");
		map.put("book_e_pdf", "阅读PDF");
//		map.put("book_e_html", "HTML");
		map.put("book_e_html", "HTML");
		map.put("book_contract", "合同");

		map.put("book_publish_soft", "排版使用的软件");
		map.put("book_send_info", "其他送货地址、联系人及备注送货册书");
		map.put("book_content_intr_cn", "内容简介（中文）");
		map.put("book_content_intr_english", "内容简介（英文）");
		map.put("book_content_intr_foreign", "内容简介（外文）");
		map.put("book_author_intr_cn", "作者简介（中文）");
		map.put("book_author_intr_english", "作者简介（英文）");
		map.put("book_author_intr_foreign", "作者简介（外文）");
		map.put("book_editor_recommend_cn", "编辑推荐（中文）");
		map.put("book_editor_recommend_english", "编辑推荐（英文）");
		map.put("book_editor_recommend_foreign", "编辑推荐（外文）");
		map.put("book_cooperate_press", "合作出版社");

		map.put("book_mark", "备注"); //2015-05-01 add
		return map;
	}

    /**
     * 数据库中英文映射
     * @return
     */
    public static Map<String, String> bookTableColumnSubMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("book_name_cn", "书名（中文）");
        map.put("book_name_english", "书名（英文）");
        map.put("book_name_foreign", "书名（外文）");
		map.put("book_name_e", "书名（阿文）");
		map.put("book_name_fa", "书名（法文）");
		map.put("book_name_xi", "书名（西文）");
        map.put("book_author", "著者（中文）");
        map.put("book_author_english", "著者（英文）");
        map.put("book_author_foreign", "著者（外文）");
		map.put("book_author_e", "著者（阿文）");
		map.put("book_author_fa", "著者（法文）");
		map.put("book_author_xi", "著者（西文）");
        map.put("book_translator", "译者（中文）");
        map.put("book_translator_english", "译者（英文）");
        map.put("book_translator_foreign", "译者（外文）");
		map.put("book_translator_e", "译者（阿文）");
		map.put("book_translator_fa", "译者（法文）");
		map.put("book_translator_xi", "译者（西文）");
		map.put("book_keyword_cn", "中文关键词");
		map.put("book_keyword_english", "英文关键词");
		map.put("book_keyword_foreign", "外文关键词");
		map.put("book_keyword_e", "关键词（阿文）");
		map.put("book_keyword_fa", "关键词（法文）");
		map.put("book_keyword_xi", "关键词（西文）");
		map.put("book_content_intr_cn", "内容简介（中文）");
		map.put("book_content_intr_english", "内容简介（英文）");
		map.put("book_content_intr_foreign", "内容简介（外文）");
		map.put("book_content_intr_a", "内容简介（阿文）");
		map.put("book_content_intr_fa", "内容简介（法文）");
		map.put("book_content_intr_xi", "内容简介（西文）");
		map.put("book_author_intr_cn", "作者简介（中文）");
		map.put("book_author_intr_english", "作者简介（英文）");
		map.put("book_author_intr_foreign", "作者简介（外文）");
		map.put("book_author_intr_e", "作者简介（阿文）");
		map.put("book_author_intr_fa", "作者简介（法文）");
		map.put("book_author_intr_xi", "作者简介（西文）");
		map.put("book_editor_recommend_cn", "编辑推荐（中文）");
		map.put("book_editor_recommend_english", "编辑推荐（英文）");
		map.put("book_editor_recommend_foreign", "编辑推荐（外文）");
		map.put("book_editor_recommend_e", "编辑推荐（阿文）");
		map.put("book_editor_recommend_fa", "编辑推荐（法文）");
		map.put("book_editor_recommend_xi", "编辑推荐（西文）");
        map.put("book_editor", "责编");
        map.put("book_clcc", "中图分类号");
        map.put("book_alcc", "美图分类号"); //0823
        map.put("book_category1", "建议分类1");
        map.put("book_category2", "建议分类2");
        map.put("book_category3", "建议分类3");
        map.put("book_series_cn", "丛书名（中文）");
        map.put("book_series_english", "丛书名（英文）");
        map.put("book_series_foreign", "丛书名（外文）");
        map.put("book_publish_count", "版次");
        map.put("book_print_count", "印次");
        map.put("book_copyright_word_count", "版权字数（千字）");
		map.put("book_publish_soft", "排版使用的软件");
		map.put("book_send_info", "其他送货地址、联系人及备注送货册书");
		map.put("book_cooperate_press", "合作出版社");
        map.put("book_size", "用纸尺寸");
        map.put("book_kaiben", "开本尺寸");
        map.put("book_heigh", "书脊厚度");
        map.put("book_publish_time", "出版时间");
        map.put("book_copyright_expires", "版权到期");
        map.put("book_image_count", "插图数量");
        map.put("book_image_spread", "插图分布");
        map.put("book_table_count", "插表数量");
        map.put("book_disk_type", "配盘介质");
        map.put("book_disk_send", "光盘赠送方式");
        map.put("book_paper_price", "纸书定价（RMB）");
        map.put("book_paper_dollar_price", "纸书定价（USD）");
        map.put("book_ebook_price", "电子书定价（RMB）");
        map.put("book_ebook_dollar_price", "电子书定价（USD）");
        map.put("book_cover_paper", "封面用纸");
        map.put("book_cover_publish_color", "封面印刷颜色");
        map.put("book_image_paper", "插页用纸");
        map.put("book_image_publish_color", "插页印刷颜色");
        map.put("book_neiwen_paper", "内文用纸");
        map.put("book_neiwen_publish_color", "内文印刷颜色");
        map.put("book_zhuangzhen_class", "装帧类型");
        map.put("book_zhuangzhen_type", "装帧方式");
        map.put("book_end_fumo", "后工艺覆膜");
        map.put("book_end_uv", "后工艺UV");
        map.put("book_end_lekou", "后工艺勒口");
        map.put("book_end_sufeng", "后工艺封装");
        map.put("book_end_other", "后工艺其他");
        map.put("book_design_cover_company", "封面设计公司");
        map.put("book_design_style_company", "版式制作公司");
        map.put("book_neiwen_page_count", "内文页数");
        map.put("book_image_page_count", "插页页数");
        map.put("book_publish_page_count", "总印张数");
        map.put("book_weight", "成书克重");
        map.put("book_publish_explain", "印装说明");
        map.put("book_news_publish_count", "新闻办印数");
        map.put("book_news_distribute_count", "发行印数");
        map.put("book_news_count", "总印数");
        map.put("book_news_shangjiao_count", "上缴样书册数");
		map.put("book_mark", "备注"); //2015-06-14 add
        return map;
    }


	public static Map<String, String> logTableColumnMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("book_serial_number", "图书编号");
		map.put("user_name", "用户");
		return map;
	}
	/**
	 * 根据列名获取中文名
	 * @param columnName
	 * @return
	 */
	public static String getBookTableCnByColumnName(String columnName) {
		Map map = bookTableColumnMap();
		return StringUtil.ObjectToString(map.get(columnName));
	}

	/**
	 * 根据中文名获取列名
	 * @param cn
	 * @return
	 */
	public static String getBookTableColumnNameByCn(String cn) {
		Map map = bookTableColumnMap();
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		String value="";
		while(it.hasNext()) {
			Entry e=(Entry)it.next();
			if(cn.equals(e.getValue())) {
				value=StringUtil.ObjectToString(e.getKey());
				break;
			}
		}
		return value;
	}


	/**
	 * 根据列名获取中文名
	 * @param columnName
	 * @return
	 */
	public static String getLogTableCnByColumnName(String columnName) {
		Map map = logTableColumnMap();
		return StringUtil.ObjectToString(map.get(columnName));
	}

	/**
	 * 根据中文名获取列名
	 * @param cn
	 * @return
	 */
	public static String getLogTableColumnNameByCn(String cn) {
		Map map = logTableColumnMap();
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		String value="";
		while(it.hasNext()) {
			Entry e=(Entry)it.next();
			if(cn.equals(e.getValue())) {
				value=StringUtil.ObjectToString(e.getKey());
				break;
			}
		}
		return value;
	}

	/**
	 * 把数据库字段转成xml
	 * @return
	 */
	public static String getTableColumnXml() {
		Map<String, String> map = bookTableColumnSubMap();
		String xml = "<root>";
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while(it.hasNext()) {
			Entry e=(Entry)it.next();
            String key = StringUtil.ObjectToString(e.getKey());
            String val = StringUtil.ObjectToString(e.getValue());
            xml+="<item cname='"+val+"' ename='"+key+"' />";
		}
        xml+="</root>";
		return xml;
	}

	public static void main(String [] args) throws Exception  {
		System.out.println(getTableColumnXml());
	}
}
