package com.atguigu.wxlogin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//启动目录

@ComponentScan({"com.atguigu"})
@SpringBootApplication
@MapperScan("com.atguigu.wxlogin.mapper")
public class ServiceWXApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceWXApplication.class, args);
    }
}
