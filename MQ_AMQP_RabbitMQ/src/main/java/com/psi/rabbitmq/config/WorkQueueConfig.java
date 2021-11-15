package com.psi.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * work消息模型队列配置类
 */
@Configuration
public class WorkQueueConfig {

    //work
    private final String work="work_queue";

    //创建work消息队列模型实例
    @Bean
    public Queue workQueue() {
        return new Queue(work);
    }
}
