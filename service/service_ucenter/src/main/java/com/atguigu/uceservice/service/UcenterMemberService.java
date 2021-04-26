package com.atguigu.uceservice.service;

import com.atguigu.uceservice.entity.UcenterMember;
import com.atguigu.uceservice.entity.vo.LoginVo;
import com.atguigu.uceservice.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-04-13
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    void register(RegisterVo registerVo);

    String login(LoginVo loginVo);
}
