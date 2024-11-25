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
    public Docket UserApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(userApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lynnwork.sobblogsystem"))
                .paths(PathSelectors.any())
                .build()
                .groupName("用户接口文档");
    }

    public ApiInfo userApiInfo() {
        return new ApiInfoBuilder()
                .title("Lynn博客系统")
                .description("Lynn博客系统接口文档")
                .version(version)
                .build();
    }
}
