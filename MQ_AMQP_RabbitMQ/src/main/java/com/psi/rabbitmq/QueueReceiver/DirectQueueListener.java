package com.psi.rabbitmq.QueueReceiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/***
 * 路由模式direct
 * 消费者只有接收到指定队列的消息
 */
@Component
public class DirectQueueListener {

    @RabbitListener(queues = "direct_queue_1")
    public void direct1(String message) {
        System.out.println("direct_1:" + message);
    }

    @RabbitListener(queues = "direct_queue_2")
    public void direct2(String message) {
        System.out.println("direct_2:" + message);
    }
}
