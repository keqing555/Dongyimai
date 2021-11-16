package com.psi.sms;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/***
 * 发送短信测试
 */
@SpringBootTest
public class TestSendSms {

    private final String simpleQueue = "dym_sms_queue";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", "18790072183");
        map.put("code", "123456");
        rabbitTemplate.convertAndSend(simpleQueue, map);
    }
}
