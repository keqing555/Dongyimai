package com.psi.alipay.controller;

import com.psi.alipay.service.AlipayService;
import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    /***
     * 返回支付二维码，订单号，金额
     * @return
     */
    @GetMapping("createNative")
    public Map<String, String> createNative() {
        IdWorker idWorker = new IdWorker();
        String out_trade_no = idWorker.nextId() + "";

        Map<String, String> map = alipayService.createNative(out_trade_no, 100);

        return map;
    }

    /***
     * 循环检测支付状态，直至支付成功或超时
     * @param out_trade_no
     * @return
     */
    @GetMapping("getPayStatus")
    public Result getPayStatus(String out_trade_no) {
        Result result = new Result();
        int count = 0;//检测次数

        while (true) {
            Map<String, String> tradeMap = alipayService.queryPayStatus(out_trade_no);
            String tradeStatus = (String) tradeMap.get("trade_status");

            if (tradeStatus != null && tradeStatus.equals("TRADE_SUCCESS")) {
                return new Result(true, StatusCode.OK, "支付成功");
            } else if (tradeStatus != null && tradeStatus.equals("TRADE_FINISHED")) {
                return new Result(true, StatusCode.OK, "交易结束");
            } else if (tradeStatus != null && tradeStatus.equals("TRADE_CLOSED")) {
                return new Result(true, StatusCode.OK, "未付款交易超时");
            }

            //每隔一秒检测一次
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            count++;
            //超过设定时间
            if (count >= 60) {
                result = new Result(false, StatusCode.ERROR, "超过一分钟未支付，已超时");
                break;
            }
        }
        return result;
    }
}
