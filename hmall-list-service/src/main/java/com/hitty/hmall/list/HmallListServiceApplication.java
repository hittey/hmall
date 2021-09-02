package com.hitty.hmall.list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hitty.hmall")
public class HmallListServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmallListServiceApplication.class, args);
    }

}
