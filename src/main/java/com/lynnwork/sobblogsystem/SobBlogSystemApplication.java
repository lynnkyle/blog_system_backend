package com.lynnwork.sobblogsystem;

import com.lynnwork.sobblogsystem.utils.SnowflakeIdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@MapperScan("com.lynnwork.sobblogsystem.mapper")
@SpringBootApplication
public class SobBlogSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SobBlogSystemApplication.class, args);
    }

    @Bean
    public SnowflakeIdWorker idWorker() {
        return new SnowflakeIdWorker(0, 0);
    }

    @Bean
    public BCryptPasswordEncoder encoderPassword() {
        return new BCryptPasswordEncoder();
    }
}
