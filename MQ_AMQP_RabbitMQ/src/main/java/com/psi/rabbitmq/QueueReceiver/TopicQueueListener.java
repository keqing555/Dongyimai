package com.psi.rabbitmq.QueueReceiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 主题模式
 * 接收匹配的通配符路由key
 */
@Component
public class TopicQueueListener {

    @RabbitListener(queues = "topic_queue_1")
    public void topic1(String message) {
        System.out.println("topic_1_" + message);
    }

    @RabbitListener(queues = "topic_queue_2")
    public void topic2(String message) {
        System.out.println("topic_2_" + message);
    }
}
