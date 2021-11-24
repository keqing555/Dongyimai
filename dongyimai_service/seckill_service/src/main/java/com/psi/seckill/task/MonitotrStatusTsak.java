package com.psi.seckill.task;

import com.psi.seckill.bean.SeckillStatus;
import com.psi.seckill.dao.SeckillGoodsMapper;
import com.psi.seckill.pojo.SeckillGoods;
import com.psi.seckill.pojo.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 监听当前订单的支付状态
 */
@Component
public class MonitotrStatusTsak {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    @Async
    public void monitorPayStatus(String username, SeckillOrder seckillOrder) {
        int n = 0;
        while (true) {
            SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
            if (seckillStatus.getStatus() == 2) {
                //处于等待支付的状态
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                n++;
                if (n >= 120) {
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
                //不等于2
                break;
            }

        }
    }
}
