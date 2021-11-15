package com.psi.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMQAPP {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQAPP.class, args);
        System.out.println("RabbitMQ启动");
    }
}
