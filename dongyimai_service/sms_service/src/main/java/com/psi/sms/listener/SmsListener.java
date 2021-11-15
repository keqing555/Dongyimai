package com.psi.sms.listener;

import com.psi.sms.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;


    public void getMessage(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            String mobile = map.get("mobile");
            String code = map.get("code");
            //发送短信
            smsUtil.sendSms(mobile, code);
        }
    }
}
