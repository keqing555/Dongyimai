package com.psi.alipay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.psi.alipay.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayClient alipayClient;


    @Override
    public Map<String, String> createNative(String out_trade_no, double total_amount) {
        Map<String, String> map = new HashMap<>();

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl("");//异步通知地址

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", out_trade_no);//订单号
        bizContent.put("total_amount", total_amount);//订单金额
        bizContent.put("subject", "测试商品");//支付商品名称
        bizContent.put("qr_code_timeout_express", "1m");//支付链接有效时间，分钟

        request.setBizContent(request.toString());

        request.setBizContent(bizContent.toString());//转成json字符串

        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        if (response.isSuccess() && "10000".equals(response.getCode())) {
            map.put("qr_code", response.getQrCode());
            map.put("out_trade_no", out_trade_no);
            map.put("total_amount", String.valueOf(total_amount));
            System.out.println("预支付接口调用成功");
        } else {
            map.put("qr_code", "");
            map.put("out_trade_no", "");
            map.put("total_amount", "0");
            System.out.println("预支付接口调用失败");
        }
        map.put("code", response.getCode());//code为10000表示调用成功
        map.put("msg", response.getMsg());

        return map;
    }

    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {
        Map<String, String> map = new HashMap<>();

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", out_trade_no);

        request.setBizContent(bizContent.toString());

        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


        if (response.isSuccess() && "10000".equals(response.getCode())) {
            map.put("code", response.getCode());//状态码：10000
            map.put("trade_status", response.getTradeStatus());//交易状态
            map.put("trade_no", response.getTradeNo());//支付成功后，返回的交易流水号，存储在支付日志的transaction_id字段
            System.out.println("调用成功");
        } else {
            map.put("code", response.getCode());//状态码：10000
            map.put("trade_status", response.getTradeStatus());//交易状态
            System.out.println("调用失败");
        }
        return map;
    }
}
