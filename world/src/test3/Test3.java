package test3;

import org.dom4j.Element;

import java.util.List;

/**
 * Created by wanghonghui on 2015/4/30.
 */
public class Test3 extends BaseTest {

    public Test3(String xmlPath) {
        super(xmlPath);
    }

    public void paserText() {
        List<Element> list = doc.selectNodes("//KeyWord");
        String str4 = "";
        String str5 = "";
        String str6 = "";

        for (Element ele : list) {
            if (ele.asXML().replaceAll("<[^<]+>", "").contains("CDATA") || ele.asXML().replaceAll("<[^<]+>", "").contains("&amp;"))
                continue;
            String keyword = ele.asXML().replaceAll("<[^<]+>", "");
            int size = ele.getParent().selectNodes("Analysis/ExplainGroup/ExplainDetail").size();
            switch (size) {
                case 4:
                    str4 += keyword + "，";
                    break;
                case 5:
                    str5 += keyword + "，";
                    break;
                case 6:
                    str6 += keyword + "，";
                    break;
            }

        }
        System.out.println(str4);
        System.out.println(str5);
        System.out.println(str6);
    }

    public static void main(String[] args) {
        String xmlPath = "\\\\172.16.1.121\\资源共享\\Cpnet20120924\\UpTools\\out\\新华多功能字典.xml";
        Test3 t = new Test3(xmlPath);
        t.paserText();
    }
}
