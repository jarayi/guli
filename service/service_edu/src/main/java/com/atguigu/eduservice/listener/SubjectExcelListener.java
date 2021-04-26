package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {


    private EduSubjectService subjectService;


    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public void invoke(ExcelSubjectData excelSubjectData,
                       AnalysisContext analysisContext) {
//1.验空
        if (excelSubjectData == null) {
            throw new GuliException(2001, "导入课程分类信息失败");
        }
        //        2.读取一级分类数据
        String oneSubjectName = excelSubjectData.getOneSubjectName();


//        3.判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(subjectService, oneSubjectName);
        if (existOneSubject == null) {
            //        4.如果数据库没有，插入数据库
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(oneSubjectName);
            subjectService.save(existOneSubject);
        }
        String pid = existOneSubject.getId();
//        5.读取耳机分类数据
        String twoSubjectName = excelSubjectData.getTwoSubjectName();
//        6.判断二级分类是否重复
        EduSubject eduTwoSubject = this.existTwoSubject(subjectService, twoSubjectName, pid);
        if (eduTwoSubject == null) {
            eduTwoSubject = new EduSubject();
            eduTwoSubject.setParentId(pid);
            eduTwoSubject.setTitle(twoSubjectName);
            subjectService.save(eduTwoSubject);
        }
//
    }

    private EduSubject existOneSubject(EduSubjectService subjectService, String oneSubjectName) {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", "0");
        queryWrapper.eq("title", oneSubjectName);
        EduSubject oneSubject = subjectService.getOne(queryWrapper);
        return oneSubject;
    }

    private EduSubject existTwoSubject(EduSubjectService subjectService,
                                       String twoSubjectName,
                                       String pid) {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", pid);
        queryWrapper.eq("title", twoSubjectName);
        EduSubject twoSubject = subjectService.getOne(queryWrapper);
        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


}
