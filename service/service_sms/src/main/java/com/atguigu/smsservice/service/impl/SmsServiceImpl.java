package com.atguigu.smsservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.smsservice.service.SmsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {

    @Override
    public boolean sendCode(String phone, Map<String, String> map) {
      if(StringUtils.isEmpty(phone))  return false;

        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI4G1xrNTTjM7CJtUNa3GC", "7hWoUbSJIlzDHNLOQ9NK9avTyoZQBd");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);//手机号
        request.putQueryParameter("SignName", "damece");//签名
        request.putQueryParameter("TemplateCode", "SMS_203196545");//模板编号
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"发送短信失败");
        }

    }
}
