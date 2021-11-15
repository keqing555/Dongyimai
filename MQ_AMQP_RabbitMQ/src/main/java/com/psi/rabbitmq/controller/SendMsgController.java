package com.psi.rabbitmq.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 队列发送端
 */
@RestController
public class SendMsgController {
    //work消息模型队列名称
    private final String work = "work_queue";
    //发布者订阅模式交换机名称
    private final String fanoutExchange = "fanout_exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /***
     * work消息模型
     * @return
     */
    @GetMapping("/work")
    public String work() {
        for (int i = 0; i < 50; i++) {
            rabbitTemplate.convertAndSend(work, "work消息队列" + i);
        }
        return "<h1>消息发送成功。。。<h1>";
    }

    /***
     * 发布订阅消息模型
     * 生产者发布消息，所有消费者都可以获取所有消息。
     * @return
     */
    @GetMapping("/fanout")
    public String fanout() {
        for (int i = 0; i < 50; i++) {
            // System.out.println("发布订阅模式消息列队" + i);
            rabbitTemplate.convertAndSend(fanoutExchange, "", "发布订阅模式消息列队" + i);
        }
        return "<h1>消息发送成功。。。<h1>";
    }

    /***
     * 路由模式消息队列
     * @return
     */
    @GetMapping("/direct/key1")
    public String direct1() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("direct_exchange",
                    "direct_key_1",
                    "消息队列key1" + i);
        }
        return "<h1>消息发送成功。。。<h1>";
    }

    @GetMapping("/direct/key2")
    public String direct2() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("direct_exchange",
                    "direct_key_2",
                    "消息队列key2" + i);
        }
        return "<h1>消息发送成功。。。<h1>";
    }

    /***
     * 主题模式Topic
     * 以统配符匹配路由key，获取不同消息
     * @return
     */
    @GetMapping("/topic/single")
    public String topic1() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("topic_exchange",
                    "topic.key",
                    "通配符[.*]:" + i);
        }
        return "<h1>消息发送成功。。。<h1>";
    }

    @GetMapping("/topic/multiple")
    public String topic2() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("topic_exchange",
                    "topic.key1.key2",
                    "通配符[.#]:" + i);
        }
        return "<h1>消息发送成功。。。<h1>";
    }

}
