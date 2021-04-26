package com.atguigu.wxlogin.controller;


import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.wxlogin.entity.UcenterMember;
import com.atguigu.wxlogin.service.UcenterMemberService;
import com.atguigu.wxlogin.utils.ConstantPropertiesUtil;
import com.atguigu.wxlogin.utils.HttpClientUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-15
 */
@Api(description = "微信扫码登录")
@Controller
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "生成微信授权登录二维码")
    @GetMapping("login")
    public String login() {
        //1拼写重定向地址
        //方式1：https://open.weixin.qq.com/connect/qrconnect?
        // appid=APPID&redirect_uri=REDIRECT_URI
        // &response_type=code&scope=SCOPE&state=STATE#wechat_redirect
        //方式2：
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                "wxatguigu");

        return "redirect:" + qrcodeUrl;

    }

    @ApiOperation(value = "微信授权登录")
    @GetMapping("callback")
    public String callback(String code, String state) {
        // 获得授权临时票据
        System.out.println("code = " + code);
        System.out.println("state = " + state);
        // 向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        String accesssTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        String result = null;
        try {
            result = HttpClientUtils.get(accesssTokenUrl);
            System.out.println("result = " + result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "获取access_token失败");
        }

        // 解析返回json串
        Gson gson = new Gson();
        HashMap map = gson.fromJson(result, HashMap.class);
        String accessToken = (String) map.get("access_token");
        String openid = (String) map.get("openid");
        System.out.println("openid = " + openid);
        System.out.println("accessToken = " + accessToken);
        //查询数据表，确认是否已注册

        QueryWrapper<UcenterMember> memberWrapper = new QueryWrapper<>();
        memberWrapper.eq("openid", openid);
        UcenterMember member = memberService.getOne(memberWrapper);
        if (member == null) {
            // 该用户未注册、
            // 根据accesstoken换取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultUserInfo=" + resultUserInfo);
            } catch (Exception e) {
                throw new GuliException(20001, "获取用户信息失败");
            }
            // 解析JSON
            HashMap mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String) mapUserInfo.get("nickname");
            String headimgurl = (String) mapUserInfo.get("headimgurl");
            // 完成注册
            member = new UcenterMember();
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            member.setIsDisabled(false);
            // 放入数据库
            memberService.save(member);
        }
        // 使用JWT生成登陆后的token
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());


        return "redirect:http://localhost:3000?jwtToken="+token;
    }
}

