package com.cp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;

public class FileUtil {
	/**
	 * 全角转半角
	 * 
	 * @param
	 * @return 半角字符串
	 */
	public static String toDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);
		return returnString;
	}

	/**
	 * xml文档
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Document getDoc(File file) {
		SAXReader read = new SAXReader();
		try {
			return read.read(file);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * xslt转xml到html
	 * 
	 * @param xmlStr
	 * @param xsltPath
	 * @return
	 * @throws Exception
	 */
	public static String xmlStrToHtml(String xmlStr, String xsltPath) throws Exception {
		Document doc = DocumentHelper.parseText(xmlStr);
		TransformerFactory factory = TransformerFactory.newInstance();
		StreamSource xsl = new StreamSource(new File(xsltPath));
		Transformer transformer = factory.newTransformer(xsl);
		Properties props = transformer.getOutputProperties();
		props.setProperty(OutputKeys.ENCODING, "UTF8");
		props.setProperty(OutputKeys.METHOD, "html");
		props.setProperty(OutputKeys.VERSION, "4.0");
		transformer.setOutputProperties(props);
		DocumentSource docSource = new DocumentSource(doc);
		StringWriter strWriter = new StringWriter();
		StreamResult docResult = new StreamResult(strWriter);
		transformer.transform(docSource, docResult);
		return strWriter.toString();
	}

	public static String fileInput(String srcPath, String charset) {
		StringBuffer sb = new StringBuffer();
		File file = new File(srcPath);
		try {
			FileInputStream stream = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(stream, charset);
			BufferedReader br = new BufferedReader(isr);
			String temp = null;
			
			while ((temp = br.readLine()) != null) {
				sb.append(temp).append("\r\n");
			}
			br.close();
			isr.close();
			stream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static void fileOutput(String filename, String charset, String result) throws IOException {
		File file = new File(filename);
		FileOutputStream fos = new FileOutputStream(file);
		Writer out = new OutputStreamWriter(fos, charset);
		out.write(result);
		if (out != null) {
			out.close();
		}
		if (fos != null) {
			fos.close();
		}
	}
}
