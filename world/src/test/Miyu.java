package test;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wanghonghui on 2015/4/22.
 */
public class Miyu {

    static StringBuffer sb = new StringBuffer();
    public static void main(String []args) throws Exception{
        test();
    }

    public  static void test() throws  Exception{
        String xml = "C:\\Users\\wanghonghui\\Desktop\\miyu.xml";
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File(xml));
        List<Element> list = doc.selectNodes("//谜语");
        int count = 0;
        for(Element ele : list) {
            count++;
            String sql ="";
            String key = ele.attributeValue("key");

            List<Element> subList = ele.selectNodes("谜面");
            if(subList==null||subList.isEmpty()) {
                sql = "insert into miyu_detail (id, miyu, mimian, mimian_count, word_leval) values("+count+" , '" +key+ "', '"+""+"',0, '一');";
            } else {
                //for(Element mimians :subList){
                //String mimian = mimians.getTextTrim();
                //sql = "insert into miyu_detail (id, miyu, mimian) values("+count+" , '" +key+ "', '"+mimian+"');";
                sql = "insert into miyu_detail (id, miyu, mimian, mimian_count, word_leval) values("+count+" , '" +key+ "', '"+ele.asXML().replaceAll(">[\r\n\\s]+<", "><")+"', "+subList.size()+", '一');";
                //}
            }
            System.out.println(sql);
        }
    }

    public static void createMiyuXml() throws Exception {
        String [] lines = fileInput("C:\\Users\\wanghonghui\\Desktop\\miyu.txt").split("@@@");
        for(String line : lines) {
            sb.append("<谜语");
            aa(line);
            sb.append("</谜语>");
        }
        System.out.print(sb.toString());
    }

    //去重不排序
    public static void aa(String str) {
        String [] s = str.split("\t");
        Set<String> set = new LinkedHashSet<String>();
        for(String ss : s) {
            set.add(ss.trim());
        }
        int i = 0;

        for(String m : set) {
            if(i==0) {
                sb.append(" key='"+m+"'>");
            } else {
                sb.append("<谜面 userid='1'>").append(m).append("</谜面>");
            }
            i++;
            //System.out.print(m + "\t");
        }
    }

    public static String fileInput(String filePath) {
        StringBuffer sb = new StringBuffer();
        File file = new File(filePath);
        try {
            FileInputStream stream = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(stream, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp).append("@@@");
            }
            br.close();
            isr.close();
            stream.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
