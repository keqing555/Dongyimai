package com.psi.sms.listener;

import com.psi.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听短信验证码消息队列
 * 收到到的验证码，使用HttpUtils.doPost发送到手机
 */
@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @RabbitListener(queues = "dym_sms_queue")
    public void getMessage(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            String mobile = map.get("mobile");
            String code = map.get("code");
            System.out.println("手机号：" + mobile);
            System.out.println("验证码：" + code);
            //发送短信
            smsUtil.sendSms(mobile, code);
        }
    }
}
