package test3;

import org.dom4j.Element;

import java.util.List;

/**
 * Created by wanghonghui on 2015/4/30.
 */
public class Test2 extends BaseTest {

    public Test2(String xmlPath) {
        super(xmlPath);
    }

    public void paserText() {
        List<Element> list = doc.selectNodes("//KeyWord[@wordage='1']");
        String str9="";
        String str10="";
        String str11="";
        String str12="";
        String str14="";
        String str15="";
        for(Element ele : list) {
            String keyword = ele.asXML().replaceAll("<[^<]+>", "");
            int size = ele.getParent().selectNodes("Analysis/ExplainGroup/ExplainDetail").size();
            switch (size){
                case 9:
                    str9+=keyword+"，";
                    break;
                case 10:
                    str10+=keyword+"，";
                    break;
                case 11:
                    str11+=keyword+"，";
                    break;
                case 12:
                    str12+=keyword+"，";
                    break;
                case 14:
                    str14+=keyword+"，";
                    break;
                case 15:
                    str15+=keyword+"，";
                    break;

            }
        }
        System.out.println(str9);
        System.out.println(str10);
        System.out.println(str11);
        System.out.println(str12);
        System.out.println(str14);
        System.out.println(str15);
    }

    public static void main(String [] args) {
        String xmlPath = "\\\\172.16.1.121\\资源共享\\Cpnet20120924\\UpTools\\out\\新华词典.xml";
        Test2 t = new Test2(xmlPath);
        t.paserText();
    }
}
