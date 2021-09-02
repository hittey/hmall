package com.hitty.hmall.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hitty.hmall")
public class HmallItemWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmallItemWebApplication.class, args);
    }

}
