package com.psi.sms.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

public class SmsController {


    private final String simpleQueue = "dym_sms_queue";

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void send() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", "18790072183");
        map.put("code", "123456");
        rabbitTemplate.convertAndSend(simpleQueue, map);
    }
}
