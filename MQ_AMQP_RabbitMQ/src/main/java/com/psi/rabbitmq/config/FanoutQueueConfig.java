package com.psi.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 发布订阅模式消息队列配置类
 * 两个消息对列绑定同一个交换机
 * 生产者发布消息，所有消费者都可以获取所有消息。
 */
@Configuration
public class FanoutQueueConfig {
    //消息队列名
    private final String fanout1 = "fanout_queue_1";
    private final String fanout2 = "fanout_queue_2";

    //交换机名
    private final String fanoutExchange = "fanout_exchange";

    /***
     * 生成队列实例
     * @return
     */
    @Bean
    public Queue fanoutQueue1() {
        return new Queue(fanout1);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue(fanout2);
    }

    /***
     * 创建交换机实例
     * @return
     */
    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(fanoutExchange);
    }

    /***
     * 队列绑定交换机
     * 也可以在可视化工具里绑定
     * @param fanoutQueue1
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindingFanoutQueue1(Queue fanoutQueue1, FanoutExchange exchange) {
        return BindingBuilder.bind(fanoutQueue1).to(exchange);
    }

    @Bean
    public Binding bindingFanoutQueue2(Queue fanoutQueue2, FanoutExchange exchange) {
        return BindingBuilder.bind(fanoutQueue2).to(exchange);
    }
}
