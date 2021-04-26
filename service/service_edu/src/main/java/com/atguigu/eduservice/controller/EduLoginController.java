package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(description = "登陆管理")

@RequestMapping("/eduuser/eduuser")
@RestController
@CrossOrigin

public class EduLoginController {
    @ApiOperation(value = "登陆")
    @PostMapping("login")
    public R login() {
        return R.ok().data("token", "admin");

    }

    //{"code":20000,"data":{"roles":["admin"],"name":
    // "admin","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"}}
    @ApiOperation(value = "用户信息")
    @GetMapping("info")
    public R info() {
        return R.ok().data("roles", "admin")
                .data("name", "admin")
                .data("admin", "\n" +
                        "\n" +
                        "https://guli-beijing.oss-cn-beijing.aliyuncs.com/Camera%20Roll/u%3D2468345199%2C3071128405%26fm%3D11%26gp%3D0.jpg");

    }
}
