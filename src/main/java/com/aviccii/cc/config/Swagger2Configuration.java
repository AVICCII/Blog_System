package com.aviccii.cc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author aviccii 2020/9/17
 * @Discrimination
 */
@Configuration
public class Swagger2Configuration {

    //version
    public static final String VERSION = "1.0.0";

    @Bean
    public Docket portalApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(portalApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aviccii.cc.controller.portal"))
                .paths(PathSelectors.any())  // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("前端门户");
    }

    private ApiInfo portalApiInfo(){
        return new ApiInfoBuilder()
                .title("博客系统门户接口文档")
                .description("门户接口文档")
                .version(VERSION)
                .build();
    }

    @Bean
    public Docket adminApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(adminApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aviccii.cc.controller.admin"))
                .paths(PathSelectors.any())
                .build()
                .groupName("管理中心");
    }

    private ApiInfo adminApiInfo(){
        return new ApiInfoBuilder()
                .title("管理中心接口文档")
                .description("管理中心接口")
                .version(VERSION)
                .build();
    }


    @Bean
    public Docket UserApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(userApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aviccii.cc.controller.user"))
                .paths(PathSelectors.any())
                .build()
                .groupName("用户中心");
    }

    private ApiInfo userApiInfo(){
        return new ApiInfoBuilder()
                .title("博客系统用户接口")
                .description("用户接口")
                .version(VERSION)
                .build();
    }

}
