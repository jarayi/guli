package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-19
 */
@Api(description = "订单管理")
@RestController
@RequestMapping("/orderservice/order")
@CrossOrigin
public class TOrderController {
    @Autowired
    private TOrderService orderService;

    @ApiOperation(value = "创建订单")
    @PostMapping("/createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId,
                         HttpServletRequest request) {
//        工具类未加入
// 生成订单
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        String orderNo = orderService.createOrder(courseId, memberId);
        return R.ok().data("orderNo", orderNo);
    }

    @ApiOperation(value = "根据订单编号查询订单")
    @GetMapping("/getOrderByNo/{orderNo}")
    public R getOrderByNo(@PathVariable String orderNo) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        TOrder order = orderService.getOne(wrapper);
        return R.ok().data("order", order);
    }

    @ApiOperation(value = "根据课程用户ID用户订单是否支付")
    @GetMapping("/isBuyCourse/{memberId}/{courseId}")
    public boolean isBuyCourse(@PathVariable("memberId") String memberId,
                               @PathVariable("courseId") String courseId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        wrapper.eq("course_id", courseId);
        wrapper.eq("status", 1);
        int count = orderService.count(wrapper);
        if (count == 0) {
            return true;
        } else {
            return false;
        }
    }
}

