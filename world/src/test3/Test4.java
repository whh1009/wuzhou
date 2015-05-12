package test3;

import org.dom4j.Element;

import java.util.List;

/**
 * Created by wanghonghui on 2015/4/30.
 */
public class Test4 extends BaseTest {

    public Test4(String xmlPath) {
        super(xmlPath);
    }

    public void paserText() {
        List<Element> list = doc.selectNodes("//KeyWord[@wordage='1']");
        String str10="";
        String str12="";
        String str11="";
        for(Element ele : list) {
//                System.out.println(ele.asXML().replaceAll("<[^<]+>", "")+"\t"+ ele.getParent().selectNodes("Analysis/ExplainGroup/ExplainDetail").size());
            String keyword = ele.asXML().replaceAll("<[^<]+>", "");
            int size = ele.getParent().selectNodes("Analysis/ExplainGroup/ExplainDetail").size();
            switch (size) {
                case 10:
                    str10 += keyword + "，";
                    break;
                case 11:
                    str11 += keyword + "，";
                    break;
                case 12:
                    str12 += keyword + "，";
                    break;
            }
        }
        System.out.println(str10);
        System.out.println(str11);
        System.out.println(str12);
    }

    public static void main(String [] args) {
        String xmlPath = "\\\\172.16.1.121\\资源共享\\Cpnet20120924\\UpTools\\out\\古汉语常用字字典.xml";
        Test4 t = new Test4(xmlPath);
        t.paserText();
    }
}
