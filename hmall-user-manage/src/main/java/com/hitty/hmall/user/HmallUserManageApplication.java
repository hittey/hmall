package com.hitty.hmall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.hitty.hmall.user.mapper")
@ComponentScan(basePackages = "com.hitty.hmall" )
public class HmallUserManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmallUserManageApplication.class, args);
    }

}
