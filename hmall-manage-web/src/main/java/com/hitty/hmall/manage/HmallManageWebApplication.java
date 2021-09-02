package com.hitty.hmall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hitty.hmall")
public class HmallManageWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmallManageWebApplication.class, args);
    }

}
