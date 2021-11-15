package com.psi.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/***
 * 主题模式topic
 * 与路由direct模式类似
 * 只是topic的key可以使用 通配符
 */
@Component
public class TopicQueueConfig {

    //队列名
    private final String topic1 = "topic_queue_1";
    private final String topic2 = "topic_queue_2";

    //交换机名称
    private final String exchange = "topic_exchange";

    //创建队列实例
    @Bean
    public Queue topicQueue1() {
        return new Queue(topic1);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue(topic2);
    }

    //创建交换机实例
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchange);
    }


    /***
     * 队列绑定到交换机
     * 指定RoutingKey,可以使用通配符
     * [.*]：匹配不多不少一个词
     * [.#]：匹配一个或多个词
     * 示例： topic.default
     *      topic.*     topic.*.key1
     *      topic.#     topic.#.key2
     */
    @Bean
    public Binding bindingTopicExchange1(@Qualifier("topicQueue1") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("topic.*");
    }


    @Bean
    public Binding bindingTopicExchange2(@Qualifier("topicQueue2") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("topic.#");
    }
}
