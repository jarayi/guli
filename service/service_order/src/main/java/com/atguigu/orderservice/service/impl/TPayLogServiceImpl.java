package com.atguigu.orderservice.service.impl;

import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.entity.TPayLog;
import com.atguigu.orderservice.mapper.TPayLogMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.service.TPayLogService;

import com.atguigu.orderservice.utils.HttpClient;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-19
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    private TOrderService orderService;

    @Override
    public Map createNative(String orderNo) {
        try {
        // 查询订单信息
        QueryWrapper<TOrder> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("order_no", orderNo);
        TOrder order = orderService.getOne(orderWrapper);
        if (order == null) {
            throw new GuliException(20001, "订单失效");
        }
        // 封装参数
        Map m = new HashMap();
        // 1、设置支付参数
        m.put("appid", "wx74862e0dfcf69954"); // 关联公众号
        m.put("mch_id", "1558950191"); // 商户号
        m.put("nonce_str", WXPayUtil.generateNonceStr());// 随机号
        m.put("body", order.getCourseTitle());// 商品名称
        m.put("out_trade_no", orderNo); // 订单号
        m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");//支付金额
        m.put("spbill_create_ip", "127.0.0.1");//
        m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
        m.put("trade_type", "NATIVE"); // 支付类型
        // 创建Httpclient对象，放入参数（xml），发送请求


        HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
        client.setHttps(true);
        client.post();
        String xml = client.getContent();
            System.out.println("xml = " + xml);
            Map<String, String> resultmap = WXPayUtil.xmlToMap(xml);

            //4、封装返回结果集

            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code",resultmap.get("result_code"));// 业务执行状态
            map.put("code_url", resultmap.get("code_url")); // 二维码链接

            //微信支付二维码2小时过期，可采取2小时未支付取消订单

            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            throw  new GuliException(20001,"生成支付二维码失败");
        }
    }
        // 查询订单状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {



        return null;
    }

    @Override
    public void updataOrderStatus(Map<String, String> map) {

    }
}
