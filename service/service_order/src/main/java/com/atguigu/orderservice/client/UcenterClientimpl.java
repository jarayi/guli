package com.atguigu.orderservice.client;

import com.atguigu.servicebase.vo.UcenterMemberForPay;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientimpl implements UcenterClient{
    @Override
    public UcenterMemberForPay getUcenterForPay(String memberId) {
        return null;
    }
}
