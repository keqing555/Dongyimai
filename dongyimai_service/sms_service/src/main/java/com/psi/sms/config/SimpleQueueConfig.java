package com.psi.sms.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 配置简单消息队列
 */
@Configuration
public class SimpleQueueConfig {


    private final String simpleQueue = "dym_sms_queue";


    @Bean
    public Queue simpleQueue() {
        return new Queue(simpleQueue);
    }
}
