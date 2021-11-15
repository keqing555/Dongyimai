package com.psi.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/***
 * 路由模式消息队列
 * 队列与交换机绑定必须指定RoutingKey（路由key）
 * 消息发送方与交换机也必须指定RoutingKey
 */
@Component
public class DirectQueueConfig {

    //队列名
    private final String direct1 = "direct_queue_1";
    private final String direct2 = "direct_queue_2";

    //交换机名称
    private final String directExchange = "direct_exchange";

    /***
     * 创建队列实例
     * @return
     */
    @Bean
    public Queue directQueue1() {
        return new Queue(direct1);
    }

    @Bean
    public Queue directQueue2() {
        return new Queue(direct2);
    }

    /***
     * 创建交换机实例
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directExchange);
    }

    /***
     * 队列绑定交换机，指定RoutingKey
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindingDirectExchange1(@Qualifier("directQueue1") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("direct_key_1");
    }

    @Bean
    public Binding bindingDirectExchange2(@Qualifier("directQueue2") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("direct_key_2");
    }
}
