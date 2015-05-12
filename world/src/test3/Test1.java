package test3;

import org.dom4j.Element;

import java.util.List;

/**
 * 现代汉语词典
 * Created by wanghonghui on 2015/4/30.
 */
public class Test1 extends BaseTest {


    public Test1(String xmlPath) {
        super(xmlPath);
    }

    public void paserText() {
        List<Element> list = doc.selectNodes("//KeyWord[@wordage='1']");
        String str17="";
        String str16="";
        String str15="";
        String str14="";
        String str13="";
        String str12="";
        String str11="";
        for(Element ele : list) {
            String keyword = ele.asXML().replaceAll("<[^<]+>", "");
            int size = ele.getParent().selectNodes("Analysis/ExplainGroup/ExplainDetail").size();
            switch (size){
                case 17:
                    str17+=keyword+"，";
                    break;
                case 16:
                    str16+=keyword+"，";
                    break;
                case 15:
                    str15+=keyword+"，";
                    break;
                case 14:
                    str14+=keyword+"，";
                    break;
                case 13:
                    str13+=keyword+"，";
                    break;
                case 12:
                    str12+=keyword+"，";
                    break;
                case 11:
                    str11+=keyword+"，";
                    break;
            }
        }
        System.out.println(str17);
        System.out.println(str16);
        System.out.println(str15);
        System.out.println(str14);
        System.out.println(str13);
        System.out.println(str12);
        System.out.println(str11);

    }

    public static void main(String [] args) {
        String xmlPath = "\\\\172.16.1.121\\资源共享\\Cpnet20120924\\UpTools\\out\\现代汉语词典.xml";
        Test1 t = new Test1(xmlPath);
        t.paserText();
    }
}
