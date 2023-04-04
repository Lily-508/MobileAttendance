package com.as.attendance_springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 如果没有在SpringBoot的配置文件使用
 * springfox.documentation.enabled=false关闭Swagger的功能
 * 可以不用@EnableOpenApi
 * @EnableWebMvc 解决SpringBoot2.6.0以上抛出的异常
 */
@Configuration
@EnableOpenApi
@EnableWebMvc
public class Swagger3Configuration {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.as.attendance_springboot.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置API基本信息,会在文档上显示
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("移动考勤系统RESTful API文档")
                .description("后端API接口")
                .contact(new Contact("xll",
                        "http://www.hfut.edu.cn/", "1135299049@qq.com"))
                .termsOfServiceUrl("服务条款")
                .version("1.0")
                .build();
    }
}
