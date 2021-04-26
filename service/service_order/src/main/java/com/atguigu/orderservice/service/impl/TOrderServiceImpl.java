package com.atguigu.orderservice.service.impl;

import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.servicebase.vo.CourseWebVoForPay;
import com.atguigu.servicebase.vo.UcenterMemberForPay;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-19
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;
    @Override
    public String createOrder(String courseId, String memberId) {
//        生成订单号
        String orderNo = OrderNoUtil.getOrderNo();

        CourseWebVoForPay courseInfoForPay = eduClient.getCourseWebVoForPay(courseId);
if(courseInfoForPay==null){
    throw  new GuliException(20001,"获取课程信息失败");
}

        UcenterMemberForPay ucenterForPay = ucenterClient.getUcenterForPay(memberId);
if(ucenterForPay==null){
    throw new GuliException(20001,"获取用户信息失败");
}
        TOrder order = new TOrder();
        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoForPay.getTitle());
        order.setCourseCover(courseInfoForPay.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseInfoForPay.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterForPay.getMobile());
        order.setNickname(ucenterForPay.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);
        return orderNo;
    }
}
