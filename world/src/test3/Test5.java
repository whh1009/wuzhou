package test3;

import org.dom4j.Element;

import java.util.List;

/**
 * Created by wanghonghui on 2015/4/30.
 */
public class Test5 extends BaseTest {

    public Test5(String xmlPath) {
        super(xmlPath);
    }

    public void paserText() {
        List<Element> list = doc.selectNodes("//KeyWord[@wordage='1']");

        String str9="";
        String str10="";
        String str11="";
        String str12="";
        String str14="";
        String str16="";

        for(Element ele : list) {
            String keyword = ele.asXML().replaceAll("<[^<]+>", "");
            int size = ele.getParent().selectNodes("Analysis/ExplainGroup/ExplainDetail").size();
            switch (size) {
                case 9:
                    str9 += keyword + "，";
                    break;
                case 10:
                    str10 += keyword + "，";
                    break;
                case 11:
                    str11 += keyword + "，";
                    break;
                case 12:
                    str12 += keyword + "，";
                    break;
                case 14:
                    str14 += keyword + "，";
                    break;
                case 16:
                    str16 += keyword + "，";
                    break;
            }
        }
        System.out.println(str9);
        System.out.println(str10);
        System.out.println(str11);
        System.out.println(str12);
        System.out.println(str14);
        System.out.println(str16);
    }

    public static void main(String [] args) {
        String xmlPath = "\\\\172.16.1.121\\资源共享\\Cpnet20120924\\UpTools\\out\\新华字典.xml";
        Test5 t = new Test5(xmlPath);
        t.paserText();
    }
}
