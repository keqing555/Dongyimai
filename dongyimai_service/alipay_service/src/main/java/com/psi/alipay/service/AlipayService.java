package com.psi.alipay.service;

import java.util.Map;

public interface AlipayService {

    /***
     * 创建预交易接口，获取支付链接
     * @param out_trade_no
     * @param total_amount
     * @return
     */
    Map<String, String> createNative(String out_trade_no, double total_amount);

    /***
     * 根据支付单号查询支付状态
     * @param out_trade_no
     * @return
     */
    Map<String, String> queryPayStatus(String out_trade_no);

    /***
     * 退款接口
     * @param trade_no 支付宝交易流水号
     * @param refund_amount 退款金额
     * @param out_trade_no 支付单号
     * @return
     */
    Map<String,String> refund(String trade_no,double refund_amount,String out_trade_no);
}
