package com.atguigu.smsservice.cotroller;

import com.atguigu.commonutils.R;

import com.atguigu.smsservice.service.SmsService;
import com.atguigu.smsservice.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(description = "短信发送")
@RestController
@RequestMapping("/edusms/send")
@CrossOrigin
public class SmsController {
 @Autowired
 private SmsService smsService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @ApiOperation(value = "短信接收")
    @GetMapping("sendCode/{phone}")
    public R sendCode(@PathVariable String phone) {
        //1访问redis，根据手机号获取验证码
        String rPhone = redisTemplate.opsForValue().get(phone);
        //2判断有验证码，直接返回
        if (rPhone != null) {
            return R.ok();
        }
        //3没有验证码生成
        String code = RandomUtil.getFourBitRandom();
        //4封装调用接口参数
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        //5发送验证码，获取返回值
        boolean isSend = smsService.sendCode(phone, map);
        //6发送成功，验证码存入redis，设定时效
        if (isSend) {
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("发送短信失败");

        }
    }

}
