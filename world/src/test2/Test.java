package test2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghonghui on 2015/4/24.
 */
public class Test {

    public static void main(String[] args) throws Exception{
        File file = new File("E:\\样章成品\\元数据.xlsx");
        File []files = file.getParentFile().listFiles();
        for(File f : files) {
            String fullName = f.getName().toLowerCase().trim();
            String name = fullName.substring(0, fullName.lastIndexOf("."));
            String extension = fullName.substring(fullName.lastIndexOf(".")+1);
            System.out.println(name);
            System.out.println(extension);
        }
    }

    public static void tt() {
        List<YuanDataEntity> list = new ArrayList<YuanDataEntity>();
        YuanDataEntity y1 = new YuanDataEntity();
        y1.setIsbn("1");
        y1.setName("a");
        y1.setWenzhong("a1");
        list.add(y1);

        YuanDataEntity y2 = new YuanDataEntity();
        y2.setIsbn("2");
        y2.setName("b");
        y2.setWenzhong("b2");
        list.add(y2);

        YuanDataEntity y3 = new YuanDataEntity();
        y3.setIsbn("3");
        y3.setName("b");
        y3.setWenzhong("c3");
        list.add(y3);

        list = removeDuplicate(list);

        System.out.println(list.size());
    }

    public static List<YuanDataEntity> removeDuplicate(List<YuanDataEntity> list) {
        if (list == null) return null;
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i).getName();
            boolean flag = false;
            for (int j = i + 1; j < list.size(); j++) {
                String name2 = list.get(j).getName();
                if (name.equals(name2)) {
                    list.remove(list.get(j));
                    flag = true;
                }
            }
            if (flag) {
                list.remove(i);
                removeDuplicate(list);
            }

        }
        return list;
    }
}


