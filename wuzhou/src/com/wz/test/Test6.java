package com.wz.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test6 {
//	static String txtPath = "E:\\Workspaces\\MyEclipse Professional 2014\\wuzhou\\src\\com\\wz\\test\\txt.txt";
	static String txtPath = "E:\\wanghonghui\\workspace2013\\wuzhou\\src\\com\\wz\\test\\txt.txt";
	public static void main(String [] args) throws Exception {
		fileInput(txtPath, "utf-8");
//		String str = "";
//		str = str.replace("", "1");
//		System.out.println(str.toCharArray().length);
	}
	// 中 英 外
	public static void fileInput(String srcPath, String charset) throws IOException {
		File file = new File(srcPath);
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(stream, charset);
		BufferedReader br = new BufferedReader(isr);
		String temp = null;
		StringBuffer sb = new StringBuffer();
		while ((temp = br.readLine()) != null) {
			
			String line [] = temp.split("\t");
//			System.out.println(line.length+"--"+temp);
			String isbn = line[0];
			String bookSerialNumber = line[1];
			String bookLanguge = line[5];
			String bookBilingual = line[6];
			String bookNameCn = line[7];
			
			String bookNameFor = line[8].replace("'", "‘").replace("\"", "");
			String bookAuthor = line[9];
			String bookEditor = line[10];
			String userId = line[12];
			String userName = line[13];
			String bookTrans = line[14];
			String wordCount = (("".equals(line[15]))?"0":line[15]);
			String pageCount = (("".equals(line[16]))?"0":line[16]);
			String paperPrice = (("".equals(line[17]))?"0":line[17]);
			String zhongtu = line[18];
			String publishTime = line[19]; //出版时间
			String printTimes = (("".equals(line[20]))?"1":line[20]); //印次
			String publishTimes = (("".equals(line[21]))?"1":line[21]); //版次
			String cover = line[22];
			String fencengpdf = line[23];
			String yuedupdf = line[25]==""?line[24]:line[25];
			String neiwen = line[26].trim();
			String word = line[28].trim();
			String sheji = line[29].trim().replace("test", "");
			
			String wz = line[4];
			String sql = "";
			if("001".equals(wz)) {// 英文
				sql = "insert into wz_book "
						+ "(book_isbn, book_serial_number, book_language, book_bilingual, book_name_cn, book_name_english, book_author, book_editor, "
						+ "user_id, book_translator, book_neiwen_page_count, book_copyright_word_count, book_paper_price, book_clcc, book_publish_time, book_print_count, book_publish_count, book_paper_cover_style_file, book_paper_publish_pdf, book_e_pdf, book_paper_neiwen_style_file, book_paper_word, book_design_style_company, "
						+ "book_neiwen_serverpath, book_cover_serverpath, book_font_serverpath, book_pdf_publish_serverpath, book_word_serverpath, book_xml_serverpath, book_epub_serverpath, book_mobi_serverpath, book_pdf_read_serverpath, book_html_serverpath, book_contract_serverpath) values"
						+ "("+isbn+", '"+bookSerialNumber+"', '"+bookLanguge+"', '"+bookBilingual+"', '"+bookNameCn+"', '"+bookNameFor+"', '"+bookAuthor+"', '"+bookEditor+"',"
						+ userId + ", '"+bookTrans+"', "+pageCount+", "+wordCount+", "+paperPrice+", '"+zhongtu+"', '"+publishTime+"', "+printTimes+", "+publishTimes+", '"+cover+"', '"+fencengpdf+"', '"+yuedupdf+"', '"+neiwen+"', '"+word+"', '"+sheji+"',"
						+ "'/"+userName+"/"+bookSerialNumber+"/排版', '/"+userName+"/"+bookSerialNumber+"/封面', '/"+userName+"/"+bookSerialNumber+"/字体', '/"+userName+"/"+bookSerialNumber+"/分层PDF', '/"+userName+"/"+bookSerialNumber+"/WORD', '/"+userName+"/"+bookSerialNumber+"/XML', '/"+userName+"/"+bookSerialNumber+"/EPUB', '/"+userName+"/"+bookSerialNumber+"/MOBI', '/"+userName+"/"+bookSerialNumber+"/阅读PDF', '/"+userName+"/"+bookSerialNumber+"/HTML', '/"+userName+"/"+bookSerialNumber+"/合同');";
			} else {
				sql = "insert into wz_book "
						+ "(book_isbn, book_serial_number, book_language, book_bilingual, book_name_cn, book_name_foreign, book_author, book_editor, "
						+ "user_id, book_translator, book_neiwen_page_count, book_copyright_word_count, book_paper_price, book_clcc, book_publish_time, book_print_count, book_publish_count, book_paper_cover_style_file, book_paper_publish_pdf, book_e_pdf, book_paper_neiwen_style_file, book_paper_word, book_design_style_company, "
						+ "book_neiwen_serverpath, book_cover_serverpath, book_font_serverpath, book_pdf_publish_serverpath, book_word_serverpath, book_xml_serverpath, book_epub_serverpath, book_mobi_serverpath, book_pdf_read_serverpath, book_html_serverpath, book_contract_serverpath) values"
						+ "("+isbn+", '"+bookSerialNumber+"', '"+bookLanguge+"', '"+bookBilingual+"', '"+bookNameCn+"', '"+bookNameFor+"', '"+bookAuthor+"', '"+bookEditor+"',"
						+ userId + ", '"+bookTrans+"', "+pageCount+", "+wordCount+", "+paperPrice+", '"+zhongtu+"', '"+publishTime+"', "+printTimes+", "+publishTimes+", '"+cover+"', '"+fencengpdf+"', '"+yuedupdf+"', '"+neiwen+"', '"+word+"', '"+sheji+"',"
						+ "'/"+userName+"/"+bookSerialNumber+"/排版', '/"+userName+"/"+bookSerialNumber+"/封面', '/"+userName+"/"+bookSerialNumber+"/字体', '/"+userName+"/"+bookSerialNumber+"/分层PDF', '/"+userName+"/"+bookSerialNumber+"/WORD', '/"+userName+"/"+bookSerialNumber+"/XML', '/"+userName+"/"+bookSerialNumber+"/EPUB', '/"+userName+"/"+bookSerialNumber+"/MOBI', '/"+userName+"/"+bookSerialNumber+"/阅读PDF', '/"+userName+"/"+bookSerialNumber+"/HTML', '/"+userName+"/"+bookSerialNumber+"/合同');";
			}
			System.out.println("delete from wz_book where book_serial_number = '"+bookSerialNumber.replace("_", "\\_")+"';");
			System.out.println(sql);
			
			
			
//			String sql = "update wz_book set book_copyright_word_count = " + (("".equals(line[15]))?"0":line[15])+", "
//											+ " book_neiwen_page_count = " + (("".equals(line[16]))?"0":line[16])+", "
//											+ " book_paper_price = " + (("".equals(line[17]))?"0":line[17])+", "
//											
//											+ " book_print_count = " + (("".equals(line[20]))?"1":line[20])+", "
//											+ " book_publish_count = " + (("".equals(line[21]))?"1":line[21])+", "
//											
//											+ " book_name_foreign = '"+line[8].replace("'", "‘").replace("\"","")+"'"
//						+ " where book_serial_number = '"+bookSerialNumber+"';";
//			System.out.println(sql);
		}
		br.close();
		isr.close();
		stream.close();
	}
}
