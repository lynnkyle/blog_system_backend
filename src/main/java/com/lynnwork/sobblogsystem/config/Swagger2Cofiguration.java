package com.lynnwork.sobblogsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class Swagger2Cofiguration {
    public static final String version = "1.0.0";

    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(userApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lynnwork.sobblogsystem.controller.user"))
                .paths(PathSelectors.any())
                .build()
                .groupName("用户中心");
    }

    public ApiInfo userApiInfo() {
        return new ApiInfoBuilder()
                .title("Lynn博客系统")
                .description("Lynn博客系统接口文档")
                .version(version)
                .build();
    }

    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(adminApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lynnwork.sobblogsystem.controller.admin"))
                .paths(PathSelectors.any())
                .build()
                .groupName("管理中心");
    }

    public ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("Lynn博客系统")
                .description("Lynn博客系统接口文档")
                .version(version)
                .build();
    }
}
