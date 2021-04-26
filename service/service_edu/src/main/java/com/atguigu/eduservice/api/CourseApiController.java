package com.atguigu.eduservice.api;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.OneSubjectVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.vo.CourseWebVoForPay;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description = "前台课程展示")
@RestController
@RequestMapping("/eduservice/courseapi")
@CrossOrigin
public class CourseApiController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    EduSubjectService subjectService;

//    @Autowired
//    private OrderClient orderClient;
    @ApiOperation(value = "前台带条件分页查询课程信息")
    @PostMapping("getCourseApiPageVo/{page}/{limit}")
    public R getCourseApiPageVo(@PathVariable Long page,
                                @PathVariable Long limit,
                                @RequestBody CourseQueryVo courseQueryVo) {
        Page<EduCourse> pageParam = new Page<>(page, limit);
        Map<String, Object> map = courseService.getCourseApiPageVo(pageParam, courseQueryVo);
        return R.ok().data(map);
    }

    @ApiOperation(value = "前台根据课程id查询课程详情信息")
    @GetMapping("getCourseApiInfo/{id}")
    public R getCourseApiInfo(@PathVariable String id, HttpServletRequest request) {
        //1课程信息+课程描述+讲师信息
        CourseWebVo courseWebVo = courseService.getCourseWebVo(id);
        //2课程大纲信息：章节+小节（树形）
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoInfo(id);

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("memberId = " + memberId);
        return R.ok().data("courseWebVo", courseWebVo)
                .data("chapterVideoList", chapterVideoList);
    }

    @ApiOperation(value = "远程电泳获取课程信息根据Id")
    @GetMapping("getCourseInfoForPay/{courseId}")
    public CourseWebVoForPay getCourseWebVoForPay(@PathVariable("courseId") String courseId) {
        CourseWebVo courseWebVo = courseService.getCourseWebVo(courseId);
        CourseWebVoForPay courseWebVoForPay = new CourseWebVoForPay();
        BeanUtils.copyProperties(courseWebVo, courseWebVoForPay);
        return courseWebVoForPay;
    }
}

