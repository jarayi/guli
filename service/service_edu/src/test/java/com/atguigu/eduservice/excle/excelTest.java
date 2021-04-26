package com.atguigu.eduservice.excle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class excelTest {
    @Test
    public void writeTest(){

    }
    private static List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("张三"+i);
            list.add(data);
        }
        return list;
    }

}
