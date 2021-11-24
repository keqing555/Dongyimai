package com.psi.alipay.feign;

import com.psi.alipay.pojo.PayLog;
import com.psi.entity.PageResult;
import com.psi.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@FeignClient(value = "dym-pay")
@RequestMapping("/alipay")
public interface AlipayFeign {

    @GetMapping("createNative")
    Map<String, String> createNative();

    @GetMapping("getPayStatus")
    Result getPayStatus(String out_trade_no);

    @PostMapping("refund")
    Map<String, String> refund(
            @RequestParam("out_trade_no") String out_trade_no,
            @RequestParam("refund_amount") double refund_amount,
            @RequestParam("trade_no") String trade_no);
}