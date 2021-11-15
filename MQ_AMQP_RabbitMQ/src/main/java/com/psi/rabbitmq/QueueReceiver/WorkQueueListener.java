package com.psi.rabbitmq.QueueReceiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WorkQueueListener {

    /***
     * work消息模型
     * 平均分配消息
     * 能者多劳模式：开启 prefetch： 1
     * @param message
     */
    @RabbitListener(queues = "work_queue")
    public void work1(String message) {
        System.out.println("work_1:" + message);
    }

    @RabbitListener(queues = "work_queue")
    public void work2(String message) {
        try {
            Thread.sleep(100);//休眠模拟低效率接收消息
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("work_2:" + message);
    }
}
