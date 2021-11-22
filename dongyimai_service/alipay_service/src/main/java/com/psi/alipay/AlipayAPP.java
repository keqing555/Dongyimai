package com.psi.alipay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.psi.alipay.dao"})
public class AlipayAPP {
    public static void main(String[] args) {
        SpringApplication.run(AlipayAPP.class, args);
        System.out.println("AliPay服务启动");
    }
}
