package com.atguigu.uceservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.servicebase.vo.UcenterMemberForPay;
import com.atguigu.uceservice.entity.UcenterMember;
import com.atguigu.uceservice.entity.vo.LoginVo;
import com.atguigu.uceservice.entity.vo.RegisterVo;
import com.atguigu.uceservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-13
 */
@Api(description = "前台会员管理")
@RestController
@CrossOrigin
@RequestMapping("/uceservice/member")
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "用户注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo) {
        String token = memberService.login(loginVo);
        return R.ok().data("token", token);
    }

    @ApiOperation(value = "根据Token获取用户信息")
    @GetMapping("getMemberByToken")
    public R getMemberByToken(HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("member", member);

    }
    @ApiOperation(value = "根据用户Id获取用户信息远程调用")
    @GetMapping("getUcenterForPay/{memberId}")
    public UcenterMemberForPay getUcenterForPay(@PathVariable("memberId")String memberId){
        UcenterMember member = memberService.getById(memberId);
        UcenterMemberForPay ucenterMemberForPay = new UcenterMemberForPay();
        BeanUtils.copyProperties(member,ucenterMemberForPay);
        return  ucenterMemberForPay;
    }


}

