package com.psi.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.psi.sellergoods.feign")
public class ItemPageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemPageApplication.class, args);
        System.out.println("ItemPageApp启动");
    }
}
