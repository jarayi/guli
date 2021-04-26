package com.atguigu.uceservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//启动目录

@ComponentScan({"com.atguigu"})
@SpringBootApplication
@MapperScan("com.atguigu.uceservice.mapper")
@EnableDiscoveryClient

public class UceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UceApplication.class, args);
    }
}
