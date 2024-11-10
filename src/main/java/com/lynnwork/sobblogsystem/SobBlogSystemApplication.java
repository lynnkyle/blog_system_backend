package com.lynnwork.sobblogsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.lynnwork.sobblogsystem.mapper")
@SpringBootApplication
public class SobBlogSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SobBlogSystemApplication.class, args);
    }

}
