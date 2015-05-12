package test3;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.io.*;

/**
 * Created by wanghonghui on 2015/4/30.
 */
public class BaseTest {
    public Document doc;

    public BaseTest(){}
    public BaseTest(String xmlPath) {
        String xml = fileInput(xmlPath);
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private static String fileInput(String filePath) {
        StringBuffer sb = new StringBuffer("<Root>");
        File file = new File(filePath);
        try {
            FileInputStream stream = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(stream, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            isr.close();
            stream.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        sb.append("</Root>");
        return sb.toString().replace("<?xml version=\"1.0\"?>", "");
    }

    public void paserText(){}
}
