package com.hitty.hmall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = "com.hitty.hmall")
@MapperScan(basePackages = "com/hitty/hmall/manage/mapper")
public class HmallManageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmallManageServiceApplication.class, args);
    }

}
