package com.atguigu.uceservice.service.impl;

import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.uceservice.entity.UcenterMember;
import com.atguigu.uceservice.entity.vo.LoginVo;
import com.atguigu.uceservice.entity.vo.RegisterVo;
import com.atguigu.uceservice.mapper.UcenterMemberMapper;
import com.atguigu.uceservice.service.UcenterMemberService;
import com.atguigu.uceservice.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-13
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public void register(RegisterVo registerVo) {
        //1 取出参数，验空
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String code = registerVo.getCode();
        String password = registerVo.getPassword();
        if(StringUtils.isEmpty(nickname)||StringUtils.isEmpty(mobile)
                ||StringUtils.isEmpty(code)||StringUtils.isEmpty(password)){
            throw new GuliException(20001,"注册信息缺失");
        }
        //2 验证手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            throw new GuliException(20001,"该手机号已经注册");
        }
        //3 验证校验码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)){
            throw new GuliException(20001,"验证码有误");
        }
        //4 MD5加密密码
        String encryptPassword = MD5.encrypt(password);
        //5 补充信息后存入数据库
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setNickname(nickname);
        ucenterMember.setPassword(encryptPassword);
        ucenterMember.setAvatar("https://guli-file-190513.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        ucenterMember.setIsDisabled(false);
        baseMapper.insert(ucenterMember);

    }

    @Override
    public String login(LoginVo loginVo) {
        //1取出参数，验空
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if(StringUtils.isEmpty(mobile)
                ||StringUtils.isEmpty(password)){
            throw new GuliException(20001,"手机号或密码有误");
        }
        //2验证手机号是否存在，并取出信息
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if(ucenterMember==null){
            throw new GuliException(20001,"手机号或密码有误");
        }
        //3验证密码，验证密文
        String encryptPassword = MD5.encrypt(password);
        if(!encryptPassword.equals(ucenterMember.getPassword())){
            throw new GuliException(20001,"手机号或密码有误");
        }

        //4生成token字符串，返回
        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());

        return jwtToken;
    }


}
