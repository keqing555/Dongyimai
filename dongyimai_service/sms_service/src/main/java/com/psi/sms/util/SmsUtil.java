package com.psi.sms.util;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsUtil {

    @Value("${sms.appcode}")
    private String appcode;

    @Value("${sms.tpl_id}")
    private String tpl_id;


    private final String host = "http://dingxin.market.alicloudapi.com";
    private final String path = "/dx/sendSms";
    private final String method = "POST";


    public HttpResponse sendSms(String mobile, String code) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);//注意：APPCODE后有一英文空格

        Map<String, String> querys = new HashMap<>();
        querys.put("mobile", mobile);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", tpl_id);//使用默认短信模板

        Map<String, String> bodys = new HashMap<>();

        HttpResponse httpResponse = null;
        try {
            //发送短信验证码
            httpResponse = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return httpResponse;
    }

}
