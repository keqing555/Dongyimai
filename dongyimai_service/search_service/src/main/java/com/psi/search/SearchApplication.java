package com.psi.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient     //开启eureka注册中心
@EnableFeignClients(basePackages = "com.psi.sellergoods.feign")//开启feign客户端
@EnableElasticsearchRepositories(basePackages = "com.psi.search.dao")//开启ES存储库
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
        System.out.println("Search服务启动");
    }
}
