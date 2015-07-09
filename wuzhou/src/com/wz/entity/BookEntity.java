package com.wz.entity;

public class BookEntity {
	public Integer book_id;
	public Integer book_type;
	public String book_serial_number;
	public String book_name_cn;
	public String book_name_english;
	public String book_name_foreign;
	public String book_name_fa;
	public String book_name_xi;
	public String book_name_e;
	public String book_author;
	public String book_author_english;
	public String book_author_foreign;
	public String book_author_fa;
	public String book_author_xi;
	public String book_author_e;
	public String book_editor;
//	public String book_editor_english;
//	public String book_editor_foreign;
//	public String book_editor_fa;
//	public String book_editor_xi;
//	public String book_editor_e;
	public String book_translator;
	public String book_translator_english;
	public String book_translator_foreign;
	public String book_translator_fa;
	public String book_translator_xi;
	public String book_translator_e;
	public String book_cooperate_press;
	public String book_isbn;
	public String book_clcc;
	public String book_alcc;
	public String book_category1;
	public String book_category2;
	public String book_category3;
	public String book_series_cn;
	public String book_series_english;
	public String book_series_foreign;
	public String book_series_fa;
	public String book_series_xi;
	public String book_series_e;
	public Integer book_publish_count;
	public Integer book_print_count;
	public Float book_copyright_word_count;
	public String book_size;
	public String book_kaiben;
	public String book_heigh;
	public String book_language;
	public String book_bilingual;
	public String book_publish_time;
	public String book_copyright_expires; //版权到期时间 
	public Integer book_image_count;
	public String book_image_spread;
	public Integer book_table_count;
	public String book_disk_type;
	public String book_disk_send;
	public Float book_paper_price; //纸质书定价
	public Float book_paper_dollar_price;
	public Float book_ebook_price; //电子书定价
	public Float book_ebook_dollar_price;
	public String book_cover_paper;
	public String book_cover_publish_color;
	public String book_image_paper;
	public String book_image_publish_color;
	public String book_neiwen_paper;
	public String book_neiwen_publish_color;
	public String book_zhuangzhen_class;
	public String book_zhuangzhen_type;
	//public String book_jiaoding;
	//public String book_qimading;
	//public String book_other;
	public String book_end_fumo;
	public String book_end_uv;
	public String book_end_lekou;
	//public String book_end_mm;
	public String book_end_sufeng;
	public String book_end_other;
	public String book_design_cover_company;
	public String book_design_style_company;
	public Integer book_neiwen_page_count;
	public Integer book_image_page_count;
	public Float book_publish_page_count;
	public Float book_weight; //实际克重
//	public Float book_evaluate_weight; //估计克重
	public String book_publish_explain;
	public Integer book_news_publish_count;
	public Integer book_news_distribute_count;
	public Integer book_news_count;
	public Integer book_news_shangjiao_count;
	public String book_keyword_cn;
	public String book_keyword_english;
	public String book_keyword_foreign;
	public String book_keyword_fa;
	public String book_keyword_xi;
	public String book_keyword_e;
	public String book_paper_neiwen_style_file; //内文
	public String book_neiwen_serverpath;
//	public String book_neiwen_localpath;
	public String book_paper_cover_style_file; //封面
	public String book_cover_serverpath;
//	public String book_cover_localpath;
	public String book_paper_font; //字体
	public String book_font_serverpath;
//	public String book_font_localpath;
	public String book_paper_publish_pdf; //分层PDF
	public String book_pdf_publish_serverpath;
//	public String book_pdf_publish_localpath;
	public String book_paper_word; //word
	public String book_word_serverpath;
//	public String book_word_localpath;
	public String book_e_xml; //xml
	public String book_xml_serverpath;
//	public String book_xml_localpath;
	public String book_e_epub; //epub
	public String book_epub_serverpath;
//	public String book_epub_localpath;
	public String book_e_mobi; //mobi
	public String book_mobi_serverpath;
//	public String book_mobi_localpath;
	public String book_e_pdf; //阅览pdf
	public String book_pdf_read_serverpath;
//	public String book_pdf_read_localpath;
	public String book_e_html; //HTML
	public String book_html_serverpath;
//	public String book_html_localpath;
	
	public String book_contract;
	public String book_contract_serverpath;
	
	//public String book_paper_image;
	public String book_publish_soft;
	public String book_send_info;
	public String book_content_intr_cn;
	public String book_content_intr_english;
	public String book_content_intr_foreign;
	public String book_content_intr_fa;
	public String book_content_intr_xi;
	public String book_content_intr_e;
	public String book_author_intr_cn;
	public String book_author_intr_english;
	public String book_author_intr_foreign;
	public String book_author_intr_fa;
	public String book_author_intr_xi;
	public String book_author_intr_e;
	public String book_editor_recommend_cn;
	public String book_editor_recommend_english;
	public String book_editor_recommend_foreign;
	public String book_editor_recommend_fa;
	public String book_editor_recommend_xi;
	public String book_editor_recommend_e;
	public Integer book_flag; //版权到期是否处理标识，已经处理设置为1，没处理设置为0
	public Integer book_del_flag; //删除图书标识，删除为1，未删除为0
	public Integer user_id;

	public String book_mark;//图书备注 2015-05-01新增

	public Integer book_old_flag; //标识是否是2014年9月份之前的书目，如果是标记1，否则为0 add-time 2015-07-08新增

	public Integer getBook_old_flag() {
		return book_old_flag;
	}

	public void setBook_old_flag(Integer book_old_flag) {
		this.book_old_flag = book_old_flag;
	}

	public String getBook_mark() {
		return book_mark;
	}

	public void setBook_mark(String book_mark) {
		this.book_mark = book_mark;
	}

	public Integer getBook_id() {
		return book_id;
	}
	public void setBook_id(Integer book_id) {
		this.book_id = book_id;
	}
	public String getBook_serial_number() {
		return book_serial_number;
	}
	public void setBook_serial_number(String book_serial_number) {
		this.book_serial_number = book_serial_number;
	}
	public String getBook_name_cn() {
		return book_name_cn;
	}
	public void setBook_name_cn(String book_name_cn) {
		this.book_name_cn = book_name_cn;
	}
	public String getBook_name_foreign() {
		return book_name_foreign;
	}
	public void setBook_name_foreign(String book_name_foreign) {
		this.book_name_foreign = book_name_foreign;
	}
	public String getBook_author() {
		return book_author;
	}
	public void setBook_author(String book_author) {
		this.book_author = book_author;
	}
	public String getBook_editor() {
		return book_editor;
	}
	public void setBook_editor(String book_editor) {
		this.book_editor = book_editor;
	}
	public String getBook_translator() {
		return book_translator;
	}
	public void setBook_translator(String book_translator) {
		this.book_translator = book_translator;
	}
	public String getBook_cooperate_press() {
		return book_cooperate_press;
	}
	public void setBook_cooperate_press(String book_cooperate_press) {
		this.book_cooperate_press = book_cooperate_press;
	}
	public String getBook_isbn() {
		return book_isbn;
	}
	public void setBook_isbn(String book_isbn) {
		this.book_isbn = book_isbn;
	}
	public String getBook_clcc() {
		return book_clcc;
	}
	public void setBook_clcc(String book_clcc) {
		this.book_clcc = book_clcc;
	}
	public String getBook_category1() {
		return book_category1;
	}
	public void setBook_category1(String book_category1) {
		this.book_category1 = book_category1;
	}
	public String getBook_category2() {
		return book_category2;
	}
	public void setBook_category2(String book_category2) {
		this.book_category2 = book_category2;
	}
	public String getBook_category3() {
		return book_category3;
	}
	public void setBook_category3(String book_category3) {
		this.book_category3 = book_category3;
	}
	public String getBook_series_cn() {
		return book_series_cn;
	}
	public void setBook_series_cn(String book_series_cn) {
		this.book_series_cn = book_series_cn;
	}
	public String getBook_series_foreign() {
		return book_series_foreign;
	}
	public void setBook_series_foreign(String book_series_foreign) {
		this.book_series_foreign = book_series_foreign;
	}
	public Integer getBook_publish_count() {
		return book_publish_count;
	}
	public void setBook_publish_count(Integer book_publish_count) {
		this.book_publish_count = book_publish_count;
	}
	public Integer getBook_print_count() {
		return book_print_count;
	}
	public void setBook_print_count(Integer book_print_count) {
		this.book_print_count = book_print_count;
	}
	public Float getBook_copyright_word_count() {
		return book_copyright_word_count;
	}
	public void setBook_copyright_word_count(Float book_copyright_word_count) {
		this.book_copyright_word_count = book_copyright_word_count;
	}
	public String getBook_size() {
		return book_size;
	}
	public void setBook_size(String book_size) {
		this.book_size = book_size;
	}
	public String getBook_kaiben() {
		return book_kaiben;
	}
	public void setBook_kaiben(String book_kaiben) {
		this.book_kaiben = book_kaiben;
	}
	public String getBook_heigh() {
		return book_heigh;
	}
	public void setBook_heigh(String book_heigh) {
		this.book_heigh = book_heigh;
	}
	public String getBook_language() {
		return book_language;
	}
	public void setBook_language(String book_language) {
		this.book_language = book_language;
	}
	public String getBook_bilingual() {
		return book_bilingual;
	}
	public void setBook_bilingual(String book_bilingual) {
		this.book_bilingual = book_bilingual;
	}
	public String getBook_publish_time() {
		return book_publish_time;
	}
	public void setBook_publish_time(String book_publish_time) {
		this.book_publish_time = book_publish_time;
	}
	public Integer getBook_image_count() {
		return book_image_count;
	}
	public void setBook_image_count(Integer book_image_count) {
		this.book_image_count = book_image_count;
	}
	public String getBook_image_spread() {
		return book_image_spread;
	}
	public void setBook_image_spread(String book_image_spread) {
		this.book_image_spread = book_image_spread;
	}
	public Integer getBook_table_count() {
		return book_table_count;
	}
	public void setBook_table_count(Integer book_table_count) {
		this.book_table_count = book_table_count;
	}
	public String getBook_disk_type() {
		return book_disk_type;
	}
	public void setBook_disk_type(String book_disk_type) {
		this.book_disk_type = book_disk_type;
	}
	public String getBook_disk_send() {
		return book_disk_send;
	}
	public void setBook_disk_send(String book_disk_send) {
		this.book_disk_send = book_disk_send;
	}
	public Float getBook_paper_price() {
		return book_paper_price;
	}
	public void setBook_paper_price(Float book_paper_price) {
		this.book_paper_price = book_paper_price;
	}
	public String getBook_cover_paper() {
		return book_cover_paper;
	}
	public void setBook_cover_paper(String book_cover_paper) {
		this.book_cover_paper = book_cover_paper;
	}
	public String getBook_keyword_english() {
		return book_keyword_english;
	}
	public void setBook_keyword_english(String book_keyword_english) {
		this.book_keyword_english = book_keyword_english;
	}
	public String getBook_cover_publish_color() {
		return book_cover_publish_color;
	}
	public void setBook_cover_publish_color(String book_cover_publish_color) {
		this.book_cover_publish_color = book_cover_publish_color;
	}
	public String getBook_image_paper() {
		return book_image_paper;
	}
	public void setBook_image_paper(String book_image_paper) {
		this.book_image_paper = book_image_paper;
	}
	public String getBook_image_publish_color() {
		return book_image_publish_color;
	}
	public void setBook_image_publish_color(String book_image_publish_color) {
		this.book_image_publish_color = book_image_publish_color;
	}
	public String getBook_neiwen_paper() {
		return book_neiwen_paper;
	}
	public void setBook_neiwen_paper(String book_neiwen_paper) {
		this.book_neiwen_paper = book_neiwen_paper;
	}
	public String getBook_neiwen_publish_color() {
		return book_neiwen_publish_color;
	}
	public void setBook_neiwen_publish_color(String book_neiwen_publish_color) {
		this.book_neiwen_publish_color = book_neiwen_publish_color;
	}
	public String getBook_zhuangzhen_type() {
		return book_zhuangzhen_type;
	}
	public void setBook_zhuangzhen_type(String book_zhuangzhen_type) {
		this.book_zhuangzhen_type = book_zhuangzhen_type;
	}
	
public String getBook_zhuangzhen_class() {
		return book_zhuangzhen_class;
	}
	public void setBook_zhuangzhen_class(String book_zhuangzhen_class) {
		this.book_zhuangzhen_class = book_zhuangzhen_class;
	}
	 
	public String getBook_end_fumo() {
		return book_end_fumo;
	}
	public void setBook_end_fumo(String book_end_fumo) {
		this.book_end_fumo = book_end_fumo;
	}
	//	public String getBook_jiaoding() {
//		return book_jiaoding;
//	}
//	public void setBook_jiaoding(String book_jiaoding) {
//		this.book_jiaoding = book_jiaoding;
//	}
//	public String getBook_qimading() {
//		return book_qimading;
//	}
//	public void setBook_qimading(String book_qimading) {
//		this.book_qimading = book_qimading;
//	}
//	public String getBook_other() {
//		return book_other;
//	}
//	public void setBook_other(String book_other) {
//		this.book_other = book_other;
//	}
	public String getBook_end_uv() {
		return book_end_uv;
	}
	public void setBook_end_uv(String book_end_uv) {
		this.book_end_uv = book_end_uv;
	}
	public String getBook_end_lekou() {
		return book_end_lekou;
	}
	public void setBook_end_lekou(String book_end_lekou) {
		this.book_end_lekou = book_end_lekou;
	}
//	public String getBook_end_mm() {
//		return book_end_mm;
//	}
//	public void setBook_end_mm(String book_end_mm) {
//		this.book_end_mm = book_end_mm;
//	}
	public String getBook_end_sufeng() {
		return book_end_sufeng;
	}
	public void setBook_end_sufeng(String book_end_sufeng) {
		this.book_end_sufeng = book_end_sufeng;
	}
	public String getBook_end_other() {
		return book_end_other;
	}
	public void setBook_end_other(String book_end_other) {
		this.book_end_other = book_end_other;
	}
	public String getBook_design_cover_company() {
		return book_design_cover_company;
	}
	public void setBook_design_cover_company(String book_design_cover_company) {
		this.book_design_cover_company = book_design_cover_company;
	}
	public String getBook_design_style_company() {
		return book_design_style_company;
	}
	public void setBook_design_style_company(String book_design_style_company) {
		this.book_design_style_company = book_design_style_company;
	}
	public Integer getBook_neiwen_page_count() {
		return book_neiwen_page_count;
	}
	public void setBook_neiwen_page_count(Integer book_neiwen_page_count) {
		this.book_neiwen_page_count = book_neiwen_page_count;
	}
	public Integer getBook_image_page_count() {
		return book_image_page_count;
	}
	public void setBook_image_page_count(Integer book_image_page_count) {
		this.book_image_page_count = book_image_page_count;
	}
	
	public Float getBook_publish_page_count() {
		return book_publish_page_count;
	}
	public void setBook_publish_page_count(Float book_publish_page_count) {
		this.book_publish_page_count = book_publish_page_count;
	}
	public Float getBook_weight() {
		return book_weight;
	}
	public void setBook_weight(Float book_weight) {
		this.book_weight = book_weight;
	}
	public String getBook_publish_explain() {
		return book_publish_explain;
	}
	public void setBook_publish_explain(String book_publish_explain) {
		this.book_publish_explain = book_publish_explain;
	}
	public Integer getBook_news_publish_count() {
		return book_news_publish_count;
	}
	public void setBook_news_publish_count(Integer book_news_publish_count) {
		this.book_news_publish_count = book_news_publish_count;
	}
	public Integer getBook_news_distribute_count() {
		return book_news_distribute_count;
	}
	public void setBook_news_distribute_count(Integer book_news_distribute_count) {
		this.book_news_distribute_count = book_news_distribute_count;
	}
	public Integer getBook_news_count() {
		return book_news_count;
	}
	public void setBook_news_count(Integer book_news_count) {
		this.book_news_count = book_news_count;
	}
	public Integer getBook_news_shangjiao_count() {
		return book_news_shangjiao_count;
	}
	public void setBook_news_shangjiao_count(Integer book_news_shangjiao_count) {
		this.book_news_shangjiao_count = book_news_shangjiao_count;
	}
	public String getBook_keyword_cn() {
		return book_keyword_cn;
	}
	public void setBook_keyword_cn(String book_keyword_cn) {
		this.book_keyword_cn = book_keyword_cn;
	}
	public String getBook_keyword_foreign() {
		return book_keyword_foreign;
	}
	public void setBook_keyword_foreign(String book_keyword_foreign) {
		this.book_keyword_foreign = book_keyword_foreign;
	}
	public String getBook_paper_neiwen_style_file() {
		return book_paper_neiwen_style_file;
	}
	public void setBook_paper_neiwen_style_file(String book_paper_neiwen_style_file) {
		this.book_paper_neiwen_style_file = book_paper_neiwen_style_file;
	}
	public String getBook_paper_cover_style_file() {
		return book_paper_cover_style_file;
	}
	public void setBook_paper_cover_style_file(String book_paper_cover_style_file) {
		this.book_paper_cover_style_file = book_paper_cover_style_file;
	}
//	public String getBook_paper_image() {
//		return book_paper_image;
//	}
//	public void setBook_paper_image(String book_paper_image) {
//		this.book_paper_image = book_paper_image;
//	}
	public String getBook_paper_font() {
		return book_paper_font;
	}
	public void setBook_paper_font(String book_paper_font) {
		this.book_paper_font = book_paper_font;
	}
	public String getBook_paper_publish_pdf() {
		return book_paper_publish_pdf;
	}
	public void setBook_paper_publish_pdf(String book_paper_publish_pdf) {
		this.book_paper_publish_pdf = book_paper_publish_pdf;
	}
	public String getBook_paper_word() {
		return book_paper_word;
	}
	public void setBook_paper_word(String book_paper_word) {
		this.book_paper_word = book_paper_word;
	}
	public String getBook_e_xml() {
		return book_e_xml;
	}
	public void setBook_e_xml(String book_e_xml) {
		this.book_e_xml = book_e_xml;
	}
	public String getBook_e_epub() {
		return book_e_epub;
	}
	public void setBook_e_epub(String book_e_epub) {
		this.book_e_epub = book_e_epub;
	}
	public String getBook_e_mobi() {
		return book_e_mobi;
	}
	public void setBook_e_mobi(String book_e_mobi) {
		this.book_e_mobi = book_e_mobi;
	}
	public String getBook_e_pdf() {
		return book_e_pdf;
	}
	public void setBook_e_pdf(String book_e_pdf) {
		this.book_e_pdf = book_e_pdf;
	}
	public String getBook_e_html() {
		return book_e_html;
	}
	public void setBook_e_html(String book_e_html) {
		this.book_e_html = book_e_html;
	}
	public String getBook_publish_soft() {
		return book_publish_soft;
	}
	public void setBook_publish_soft(String book_publish_soft) {
		this.book_publish_soft = book_publish_soft;
	}
	public String getBook_send_info() {
		return book_send_info;
	}
	public void setBook_send_info(String book_send_info) {
		this.book_send_info = book_send_info;
	}
	public String getBook_content_intr_cn() {
		return book_content_intr_cn;
	}
	public void setBook_content_intr_cn(String book_content_intr_cn) {
		this.book_content_intr_cn = book_content_intr_cn;
	}
	public String getBook_content_intr_foreign() {
		return book_content_intr_foreign;
	}
	public void setBook_content_intr_foreign(String book_content_intr_foreign) {
		this.book_content_intr_foreign = book_content_intr_foreign;
	}
	public String getBook_author_intr_cn() {
		return book_author_intr_cn;
	}
	public void setBook_author_intr_cn(String book_author_intr_cn) {
		this.book_author_intr_cn = book_author_intr_cn;
	}
	public String getBook_author_intr_foreign() {
		return book_author_intr_foreign;
	}
	public void setBook_author_intr_foreign(String book_author_intr_foreign) {
		this.book_author_intr_foreign = book_author_intr_foreign;
	}
	public String getBook_editor_recommend_cn() {
		return book_editor_recommend_cn;
	}
	public void setBook_editor_recommend_cn(String book_editor_recommend_cn) {
		this.book_editor_recommend_cn = book_editor_recommend_cn;
	}
	public String getBook_editor_recommend_foreign() {
		return book_editor_recommend_foreign;
	}
	public void setBook_editor_recommend_foreign(
			String book_editor_recommend_foreign) {
		this.book_editor_recommend_foreign = book_editor_recommend_foreign;
	}
	public String getBook_name_english() {
		return book_name_english;
	}
	public void setBook_name_english(String book_name_english) {
		this.book_name_english = book_name_english;
	}
	public String getBook_author_english() {
		return book_author_english;
	}
	public void setBook_author_english(String book_author_english) {
		this.book_author_english = book_author_english;
	}
	public String getBook_author_foreign() {
		return book_author_foreign;
	}
	public void setBook_author_foreign(String book_author_foreign) {
		this.book_author_foreign = book_author_foreign;
	}
	public String getBook_translator_english() {
		return book_translator_english;
	}
	public void setBook_translator_english(String book_translator_english) {
		this.book_translator_english = book_translator_english;
	}
	public String getBook_translator_foreign() {
		return book_translator_foreign;
	}
	public void setBook_translator_foreign(String book_translator_foreign) {
		this.book_translator_foreign = book_translator_foreign;
	}
	public String getBook_series_english() {
		return book_series_english;
	}
	public void setBook_series_english(String book_series_english) {
		this.book_series_english = book_series_english;
	}
	public String getBook_content_intr_english() {
		return book_content_intr_english;
	}
	public void setBook_content_intr_english(String book_content_intr_english) {
		this.book_content_intr_english = book_content_intr_english;
	}
	public String getBook_author_intr_english() {
		return book_author_intr_english;
	}
	public void setBook_author_intr_english(String book_author_intr_english) {
		this.book_author_intr_english = book_author_intr_english;
	}
	public String getBook_editor_recommend_english() {
		return book_editor_recommend_english;
	}
	public void setBook_editor_recommend_english(String book_editor_recommend_english) {
		this.book_editor_recommend_english = book_editor_recommend_english;
	}
	public String getBook_copyright_expires() {
		return book_copyright_expires;
	}
	public void setBook_copyright_expires(String book_copyright_expires) {
		this.book_copyright_expires = book_copyright_expires;
	}
	public Integer getBook_flag() {
		return book_flag;
	}
	public void setBook_flag(Integer book_flag) {
		this.book_flag = book_flag;
	}
	public Integer getBook_del_flag() {
		return book_del_flag;
	}
	public void setBook_del_flag(Integer book_del_flag) {
		this.book_del_flag = book_del_flag;
	}
	public Float getBook_ebook_price() {
		return book_ebook_price;
	}
	public void setBook_ebook_price(Float book_ebook_price) {
		this.book_ebook_price = book_ebook_price;
	}
	public String getBook_neiwen_serverpath() {
		return book_neiwen_serverpath;
	}
	public void setBook_neiwen_serverpath(String book_neiwen_serverpath) {
		this.book_neiwen_serverpath = book_neiwen_serverpath;
	}
	
	public String getBook_cover_serverpath() {
		return book_cover_serverpath;
	}
	public void setBook_cover_serverpath(String book_cover_serverpath) {
		this.book_cover_serverpath = book_cover_serverpath;
	}
	
	public String getBook_font_serverpath() {
		return book_font_serverpath;
	}
	public void setBook_font_serverpath(String book_font_serverpath) {
		this.book_font_serverpath = book_font_serverpath;
	}
	
	public String getBook_pdf_publish_serverpath() {
		return book_pdf_publish_serverpath;
	}
	public void setBook_pdf_publish_serverpath(String book_pdf_publish_serverpath) {
		this.book_pdf_publish_serverpath = book_pdf_publish_serverpath;
	}
	
	public String getBook_word_serverpath() {
		return book_word_serverpath;
	}
	public void setBook_word_serverpath(String book_word_serverpath) {
		this.book_word_serverpath = book_word_serverpath;
	}
	
	public String getBook_xml_serverpath() {
		return book_xml_serverpath;
	}
	public void setBook_xml_serverpath(String book_xml_serverpath) {
		this.book_xml_serverpath = book_xml_serverpath;
	}
	
	public String getBook_epub_serverpath() {
		return book_epub_serverpath;
	}
	public void setBook_epub_serverpath(String book_epub_serverpath) {
		this.book_epub_serverpath = book_epub_serverpath;
	}
	
	public String getBook_mobi_serverpath() {
		return book_mobi_serverpath;
	}
	public void setBook_mobi_serverpath(String book_mobi_serverpath) {
		this.book_mobi_serverpath = book_mobi_serverpath;
	}
	
	public String getBook_pdf_read_serverpath() {
		return book_pdf_read_serverpath;
	}
	public void setBook_pdf_read_serverpath(String book_pdf_read_serverpath) {
		this.book_pdf_read_serverpath = book_pdf_read_serverpath;
	}
	
	public String getBook_html_serverpath() {
		return book_html_serverpath;
	}
	public void setBook_html_serverpath(String book_html_serverpath) {
		this.book_html_serverpath = book_html_serverpath;
	}
	
	public Integer getBook_type() {
		return book_type;
	}
	public void setBook_type(Integer book_type) {
		this.book_type = book_type;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Float getBook_paper_dollar_price() {
		return book_paper_dollar_price;
	}
	public void setBook_paper_dollar_price(Float book_paper_dollar_price) {
		this.book_paper_dollar_price = book_paper_dollar_price;
	}
	public Float getBook_ebook_dollar_price() {
		return book_ebook_dollar_price;
	}
	public void setBook_ebook_dollar_price(Float book_ebook_dollar_price) {
		this.book_ebook_dollar_price = book_ebook_dollar_price;
	}
	public String getBook_alcc() {
		return book_alcc;
	}
	public void setBook_alcc(String book_alcc) {
		this.book_alcc = book_alcc;
	}
	public String getBook_name_fa() {
		return book_name_fa;
	}
	public void setBook_name_fa(String book_name_fa) {
		this.book_name_fa = book_name_fa;
	}
	public String getBook_name_xi() {
		return book_name_xi;
	}
	public void setBook_name_xi(String book_name_xi) {
		this.book_name_xi = book_name_xi;
	}
	public String getBook_name_e() {
		return book_name_e;
	}
	public void setBook_name_e(String book_name_e) {
		this.book_name_e = book_name_e;
	}
	public String getBook_author_fa() {
		return book_author_fa;
	}
	public void setBook_author_fa(String book_author_fa) {
		this.book_author_fa = book_author_fa;
	}
	public String getBook_author_xi() {
		return book_author_xi;
	}
	public void setBook_author_xi(String book_author_xi) {
		this.book_author_xi = book_author_xi;
	}
	public String getBook_author_e() {
		return book_author_e;
	}
	public void setBook_author_e(String book_author_e) {
		this.book_author_e = book_author_e;
	}
	public String getBook_translator_fa() {
		return book_translator_fa;
	}
	public void setBook_translator_fa(String book_translator_fa) {
		this.book_translator_fa = book_translator_fa;
	}
	public String getBook_translator_xi() {
		return book_translator_xi;
	}
	public void setBook_translator_xi(String book_translator_xi) {
		this.book_translator_xi = book_translator_xi;
	}
	public String getBook_translator_e() {
		return book_translator_e;
	}
	public void setBook_translator_e(String book_translator_e) {
		this.book_translator_e = book_translator_e;
	}
	public String getBook_series_fa() {
		return book_series_fa;
	}
	public void setBook_series_fa(String book_series_fa) {
		this.book_series_fa = book_series_fa;
	}
	public String getBook_series_xi() {
		return book_series_xi;
	}
	public void setBook_series_xi(String book_series_xi) {
		this.book_series_xi = book_series_xi;
	}
	public String getBook_series_e() {
		return book_series_e;
	}
	public void setBook_series_e(String book_series_e) {
		this.book_series_e = book_series_e;
	}
	public String getBook_keyword_fa() {
		return book_keyword_fa;
	}
	public void setBook_keyword_fa(String book_keyword_fa) {
		this.book_keyword_fa = book_keyword_fa;
	}
	public String getBook_keyword_xi() {
		return book_keyword_xi;
	}
	public void setBook_keyword_xi(String book_keyword_xi) {
		this.book_keyword_xi = book_keyword_xi;
	}
	public String getBook_keyword_e() {
		return book_keyword_e;
	}
	public void setBook_keyword_e(String book_keyword_e) {
		this.book_keyword_e = book_keyword_e;
	}
	public String getBook_content_intr_fa() {
		return book_content_intr_fa;
	}
	public void setBook_content_intr_fa(String book_content_intr_fa) {
		this.book_content_intr_fa = book_content_intr_fa;
	}
	public String getBook_content_intr_xi() {
		return book_content_intr_xi;
	}
	public void setBook_content_intr_xi(String book_content_intr_xi) {
		this.book_content_intr_xi = book_content_intr_xi;
	}
	public String getBook_content_intr_e() {
		return book_content_intr_e;
	}
	public void setBook_content_intr_e(String book_content_intr_e) {
		this.book_content_intr_e = book_content_intr_e;
	}
	public String getBook_author_intr_fa() {
		return book_author_intr_fa;
	}
	public void setBook_author_intr_fa(String book_author_intr_fa) {
		this.book_author_intr_fa = book_author_intr_fa;
	}
	public String getBook_author_intr_xi() {
		return book_author_intr_xi;
	}
	public void setBook_author_intr_xi(String book_author_intr_xi) {
		this.book_author_intr_xi = book_author_intr_xi;
	}
	public String getBook_author_intr_e() {
		return book_author_intr_e;
	}
	public void setBook_author_intr_e(String book_author_intr_e) {
		this.book_author_intr_e = book_author_intr_e;
	}
	public String getBook_editor_recommend_fa() {
		return book_editor_recommend_fa;
	}
	public void setBook_editor_recommend_fa(String book_editor_recommend_fa) {
		this.book_editor_recommend_fa = book_editor_recommend_fa;
	}
	public String getBook_editor_recommend_xi() {
		return book_editor_recommend_xi;
	}
	public void setBook_editor_recommend_xi(String book_editor_recommend_xi) {
		this.book_editor_recommend_xi = book_editor_recommend_xi;
	}
	public String getBook_editor_recommend_e() {
		return book_editor_recommend_e;
	}
	public void setBook_editor_recommend_e(String book_editor_recommend_e) {
		this.book_editor_recommend_e = book_editor_recommend_e;
	}
	public String getBook_contract() {
		return book_contract;
	}
	public void setBook_contract(String book_contract) {
		this.book_contract = book_contract;
	}
	public String getBook_contract_serverpath() {
		return book_contract_serverpath;
	}
	public void setBook_contract_serverpath(String book_contract_serverpath) {
		this.book_contract_serverpath = book_contract_serverpath;
	}
}
