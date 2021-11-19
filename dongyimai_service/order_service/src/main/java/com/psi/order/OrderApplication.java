package com.psi.order;

import com.psi.order.config.FeignInterceptor;
import com.psi.utils.IdWorker;
import com.psi.utils.TokenDecode;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.psi.order.dao"})
@EnableFeignClients(basePackages = {"com.psi.sellergoods.feign"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
        System.out.println("Order服务启动");
    }

    /***
     * 创建拦截器对象
     * @return
     */
    @Bean
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }

    /***
     * 声明common模块下的TokenDecode工具类
     * @return
     */
    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }

    /**
     * 基于雪花算法的id生成器
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
