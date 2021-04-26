package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-04-19
 */
public interface TPayLogService extends IService<TPayLog> {

    Map createNative(String orderNo);

    Map<String, String> queryPayStatus(String orderNo);

    void updataOrderStatus(Map<String, String> map);
}
