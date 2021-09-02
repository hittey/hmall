package com.hitty.hmall.passport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hitty.hmall")
public class HmallPassportWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmallPassportWebApplication.class, args);
    }

}
