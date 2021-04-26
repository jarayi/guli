package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;


import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-03-29
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/eduteacher")
@CrossOrigin
public class EduTeacherController {
    @Autowired
    private EduTeacherService TeacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping
    public R getAllteacher() {

        List<EduTeacher> List = TeacherService.list(null);
        return R.ok().data("list", List);
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R delTeacher(@PathVariable String id) {
        boolean remove = TeacherService.removeById(id);
        if (remove) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("getTeacherPage/{page}/{limit}")
    public R getTeacherPage(@PathVariable Long page,
                            @PathVariable Long limit) {
        Page<EduTeacher> pageParam = new Page<>(page,limit);
        TeacherService.page(pageParam, null);
        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("records", records).data("total", total);
    }

    @ApiOperation(value = "带条件分页查询讲师")
    @PostMapping("getTeacherPageVo/{page}/{limit}")
    public R getTeacherPageVo(@PathVariable Long page,
                              @PathVariable Long limit,
                              @RequestBody TeacherQuery teacherQuery) {
        //@RequestBody 把json转化成对象
        //1 取出查询条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //2 判断查询条件是否为空
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            queryWrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end);
        }
        //3分页查询
        Page<EduTeacher> pageParam = new Page<>(page, limit);
        TeacherService.page(pageParam, queryWrapper);
        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        //方式一
//        Map<String,Object> map = new HashMap<>();
//        map.put("records",records);
//        map.put("total",total);
//        return R.ok().data(map);
        //方式二
        return R.ok().data("records", records).data("total", total);
    }
    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save =TeacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else {
            return R.error();
        }
    }
    
    @ApiOperation(value = "根据ID查询讲师信息")
    @GetMapping("getTeacherByid/{id}")
    public R getTeacherById(@PathVariable String id){
        EduTeacher eduTeacher = TeacherService.getById(id);
        return R.ok().data("eduTeacher",eduTeacher);
    }
    @ApiOperation("修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean update =TeacherService.updateById(eduTeacher);
        if(update){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

