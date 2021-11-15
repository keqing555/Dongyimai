package com.psi.rabbitmq.QueueReceiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/***
 * 发布订阅模式
 * 生产者发布消息，所有消费者都可以获取所有消息。
 */
@Component
public class FanoutQueueListener {

    @RabbitListener(queues = "fanout_queue_1")
    public void receiver1(String message) {
        System.out.println("fanout1:" + message);
    }

    @RabbitListener(queues = "fanout_queue_2")
    public void receiver2(String message) {
        System.out.println("fanout2:" + message);
    }
}
