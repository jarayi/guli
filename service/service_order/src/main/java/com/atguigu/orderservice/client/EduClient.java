package com.atguigu.orderservice.client;

import com.atguigu.servicebase.vo.CourseWebVoForPay;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-edu")
public interface EduClient {

    @GetMapping("/eduservice/courseapi/getCourseInfoForPay/{courseId}")
    public CourseWebVoForPay getCourseWebVoForPay(@PathVariable("courseId") String courseId);


}