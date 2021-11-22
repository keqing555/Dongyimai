package com.psi.alipay;

import com.psi.alipay.config.FeignInterceptor;
import com.psi.utils.TokenDecode;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.psi.order.feign"})
//@MapperScan(basePackages = {"com.psi.alipay.dao"})
public class AlipayAPP {
    public static void main(String[] args) {
        SpringApplication.run(AlipayAPP.class, args);
        System.out.println("AliPay服务启动");
    }

    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }

    @Bean
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }
}
