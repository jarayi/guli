package com.atguigu.eduservice.excle;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DemoData {
    @ExcelProperty("学生编号")
    private  int son;
    @ExcelProperty("学生姓名")
    private  String sname;

    public void setSno(int i) {
    }
}
