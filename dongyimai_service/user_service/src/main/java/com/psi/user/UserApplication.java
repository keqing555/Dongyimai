package com.psi.user;

import com.psi.utils.TokenDecode;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan(basePackages = {"com.psi.user.dao"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        System.out.println("User服务启动");
    }

    /***
     * 声明TokenDecode
     * @return
     */
    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }
}
