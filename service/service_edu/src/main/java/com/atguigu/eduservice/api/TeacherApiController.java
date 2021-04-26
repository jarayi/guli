package com.atguigu.eduservice.api;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "前台名师展示")
@RestController
@RequestMapping("/eduservice/teacherapi")
@CrossOrigin
public class TeacherApiController {
    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "分页查询")
    @GetMapping("getTeacherApiPage/{page}/{limit}")
    public R getTeacherApiPage(@PathVariable Long page,
                               @PathVariable Long limit) {
        Page<EduTeacher> pageParam = new Page<>(page, limit);
        Map<String, Object> techerMap = teacherService.getTeacherApiPage(pageParam);
        return R.ok().data(techerMap);
    }

    @ApiOperation(value = "前台讲师详情、相关课程")
    @GetMapping("getTeacherApiCourse/{id}")
    public R getTeacherApiCourse(@PathVariable String id) {
        // 根据讲师Id查询相关信息
        EduTeacher eduTeacher = teacherService.getById(id);
        //根据讲师ID查询相关课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        wrapper.eq("status", "Normal");
        wrapper.eq("teacher_id", id);
        wrapper.orderByDesc("gmt_create");
        List<EduCourse> courseList = courseService.list(wrapper);
        return R.ok().data("eduTeacher",eduTeacher).data("courseList",courseList);
    }

}
