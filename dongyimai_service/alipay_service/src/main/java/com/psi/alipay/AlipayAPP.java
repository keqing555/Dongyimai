package com.psi.alipay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AlipayAPP {
    public static void main(String[] args) {
        SpringApplication.run(AlipayAPP.class, args);
        System.out.println("AliPay服务启动");
    }
}
