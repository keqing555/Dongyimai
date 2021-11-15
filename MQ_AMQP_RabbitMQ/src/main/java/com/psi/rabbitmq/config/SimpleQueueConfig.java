package com.psi.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/***
 * 简单消息队列配置
 */
@Component
public class SimpleQueueConfig {

    //简单队列名
    private final String simpleQueue = "simple_queue";

    @Bean
    public Queue simpleQueue() {
        return new Queue(simpleQueue);
    }
}
