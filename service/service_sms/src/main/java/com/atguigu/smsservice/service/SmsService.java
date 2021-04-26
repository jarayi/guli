package com.atguigu.smsservice.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

public interface SmsService{

    boolean sendCode(String phone, Map<String, String> map);
}
