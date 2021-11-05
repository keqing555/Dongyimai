package com.psi.sellergoods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.psi.sellergoods.dao")
public class SellerGoodsApp {
    public static void main(String[] args) {
        SpringApplication.run(SellerGoodsApp.class, args);
        System.out.println("SellerGoodsApp启动");
    }
}
