package com.psi.rabbitmq.QueueReceiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/***
 * 简单消息队列
 * 逐个获取队列里的消息
 */
@Component
public class SimpleQueueListener {

    @RabbitListener(queues = "simple_queue")
    public void simple(String message) {
        System.out.println(message);
    }
}
