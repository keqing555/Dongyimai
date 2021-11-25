package com.psi.seckill.task;

import com.psi.alipay.feign.AlipayFeign;
import com.psi.entity.Result;
import com.psi.order.feign.OrderFeign;
import com.psi.order.pojo.PayLog;
import com.psi.seckill.bean.SeckillStatus;
import com.psi.seckill.dao.SeckillGoodsMapper;
import com.psi.seckill.pojo.SeckillGoods;
import com.psi.seckill.pojo.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听当前订单的支付状态
 */
@Component
public class MonitotrStatusTsak {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private OrderFeign orderFeign;

    @Autowired
    private AlipayFeign alipayFeign;

    @Async
    public void monitorPayStatus(String username, SeckillOrder seckillOrder) {
        int n = 0;
        while (true) {
            SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
            //秒杀状态  1:排队中，2:秒杀等待支付,3:支付超时，4:秒杀失败,5:支付完成
            if (seckillStatus.getStatus() == 2) {
                //处于等待支付的状态
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                n++;
                if (n >= 120) {
                    System.out.println("支付超时，停止监控");
                    //支付超时
                    seckillStatus.setStatus(3);
                    redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);
                    //恢复库存
                    Long goodsId = seckillStatus.getGoodsId();
                    SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).get(goodsId);
                    if (goods == null) {
                        goods = seckillGoodsMapper.selectById(goodsId);//stockcount=0
                    }
                    goods.setStockCount(goods.getStockCount() + 1);
                    redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).put(goodsId, goods);

                    //删除redis订单
                    redisTemplate.boundHashOps("SeckillOrder").delete(username);
                }
            } else {
                //不等于2也于等于3，即秒杀订单支付成功，
                // 把支付日志保存到MySQL，并删除redis里的支付日志
                PayLog payLog = (PayLog) redisTemplate.boundHashOps("payLog").get(username);
                Result payStatus = alipayFeign.getPayStatus(payLog.getOutTradeNo());
                Map map = (Map) payStatus.getData();
                String trade_no = map.get("trade_no") + "";
                if (payLog != null) {
                    orderFeign.updateOrderStatus(payLog.getOutTradeNo(), trade_no);
                }
                break;
            }

        }
    }
}
