package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-06
 */
@Api(description = "课程管理")
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "查询所有课程信息")
    @GetMapping("getCourseInfo")
    // TODO 实现带条件分页课程查询
    public R getCourseInfo(){
        List<EduCourse> courseList=courseService.list(null);
        return R.ok().data("courseList",courseList);

    }

    @ApiOperation(value = "添加课程信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoForm courseInfoForm) {
        String courseId = courseService.addCourseInfo(courseInfoForm);
        return R.ok().data("courseId", courseId);
    }

    @ApiOperation(value = "根据Id查询课程信息")
    @GetMapping("getCourseInfoById/{courseId}")
    public R getCourseInfoById(@PathVariable String courseId) {
        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(courseId);
        return R.ok().data("courseInfo", courseInfoForm);
    }

    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoForm courseInfoForm) {
        courseService.updateCourseInfo(courseInfoForm);
        return R.ok();
    }
    @ApiOperation(value = "根据课程ID查询课程信息")
    @GetMapping("getCoursePublishById/{courseId}")
    public R getCoursePublishById(@PathVariable String courseId){
       CoursePublishVo coursePublishVo= courseService.getCoursePublishById(courseId);

       return  R.ok().data("coursePublishVo",coursePublishVo);

    }
    @ApiOperation(value = "根据课程ID发布信息")
    @PostMapping("publishCourseById/{courseId}")
    public R publishCourseById(@PathVariable String courseId){
        EduCourse eduCourse=courseService.getById(courseId);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }
    @ApiOperation(value = "根据课程id删除课程相关")
    @DeleteMapping("delCourseInfoById/{courseId}")
    public R delCourseInfoById(@PathVariable String courseId){
        courseService.delCourseInfoById(courseId);
        return  R.ok();
    }

}

