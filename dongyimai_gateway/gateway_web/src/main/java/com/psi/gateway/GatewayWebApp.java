package com.psi.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GatewayWebApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayWebApp.class, args);
        System.out.println("WEB网关启动");
    }
}
