package com.psi.content.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2     //开启Swagger2自动生成文档的功能
public class SwaggerConfig {
    //配置文档属性
    private ApiInfo getApiInfo() {
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title("内容微服务接口文档");
        apiInfoBuilder.description("给前端妹妹看的api文档");
        apiInfoBuilder.version("1.0");
        apiInfoBuilder.termsOfServiceUrl("http://www.dongyimai.com");
        ApiInfo apiInfo = apiInfoBuilder.build();
        return apiInfo;
    }


    //创建文档配置对象
    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(getApiInfo());
        docket.groupName("Java");
        return docket.select().build();
    }
}
