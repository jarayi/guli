package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-08
 */
@Api("小节管理")
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService VideoService;

    @ApiOperation(value = "添加小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        VideoService.save(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "根据ID删除小节")
    @DeleteMapping("delVideo/{id}")
    public R delVideo(@PathVariable String id) {
        VideoService.delVideoById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查询小节")
    @GetMapping("getVideoById/{id}")
    public R getVideoById(@PathVariable String id) {
        EduVideo eduVideo = VideoService.getById(id);
        return R.ok().data("eduVideo", eduVideo);
    }

    @ApiOperation(value = "修改小节")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        VideoService.updateById(eduVideo);
        return R.ok();
    }
}

