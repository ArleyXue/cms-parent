package com.arley.cms.console;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.arley.cms.console.mapper")
public class CmsConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsConsoleApplication.class, args);
    }
}
