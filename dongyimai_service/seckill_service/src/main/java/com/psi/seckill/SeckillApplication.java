package com.psi.seckill;

import com.psi.utils.IdWorker;
import com.psi.utils.TokenDecode;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
//@EnableFeignClients(basePackages = {"com.psi.seckill.feign"})
@MapperScan(basePackages = {"com.psi.seckill.dao"})
@EnableScheduling   //开启定时任务
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
        System.out.println("秒杀服务启动");
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }

    @Bean
    public TokenDecode tokenDecode(){
        return new TokenDecode();
    }
}
