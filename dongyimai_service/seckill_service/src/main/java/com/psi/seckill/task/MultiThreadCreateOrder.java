package com.psi.seckill.task;

import com.psi.order.pojo.PayLog;
import com.psi.seckill.bean.SeckillStatus;
import com.psi.seckill.dao.SeckillGoodsMapper;
import com.psi.seckill.pojo.SeckillGoods;
import com.psi.seckill.pojo.SeckillOrder;
import com.psi.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class MultiThreadCreateOrder {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private MonitotrStatusTsak monitotrStatusTsak;

    /**
     * 多线程下单时，调用此方法，
     * 如果下单阻塞了，说明属于异步操作
     * 如果没有阻塞，说明没有执行异步操作
     */
    @Async  //异步方法，没有返回值
    public void createOrder() {
//        try {
//            System.out.println("准备执行异步下单...");
//            Thread.sleep(10000);
//            System.out.println("开始执行异步下单...");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //从redis队列中获取排队信息，右取方式
        //秒杀状态  1:排队中，2:秒杀等待支付,3:支付超时，4:秒杀失败,5:支付完成
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();


        if (seckillStatus != null) {
            String time = seckillStatus.getTime();
            String username = seckillStatus.getUsername();
            Long id = seckillStatus.getGoodsId();

            //获取商品数据
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + time).get(id);

            if (seckillGoods == null) {
                seckillStatus.setStatus(4);//秒杀失败
                redisTemplate.boundHashOps("SeckillQueueStatus").put(username, seckillStatus);
                throw new RuntimeException("该秒杀商品已过时");//秒杀时间段已过
            }
            if (seckillGoods.getStockCount() <= 0) {
                seckillStatus.setStatus(4);//秒杀失败
                redisTemplate.boundHashOps("SeckillQueueStatus").put(username, seckillStatus);
                throw new RuntimeException("该秒杀商品已经售馨");//没有库存
            }

            /**
             * 对该商品加锁
             */
            Long goodsId = seckillGoods.getId();
            Object goods = redisTemplate.boundValueOps(goodsId).get();

            //这个商品别人（其他线程）在下订单，当前线程不允许使用
            int count = 0;
            while (goods != null) {
                if (count > 100) {
                    //一直获取不到锁
                    seckillStatus.setStatus(4);//秒杀失败
                    redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);
                    throw new RuntimeException("秒杀失败");
                }
                //当获取不到这个商品的时候，说明线程被释放了
                goods = redisTemplate.boundValueOps(goodsId).get();
                count++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                //获取该商品的锁
                redisTemplate.boundValueOps(goodsId).set("lock");

                /**
                 * 有库存,创建秒杀订单
                 */
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setId(idWorker.nextId());
                seckillOrder.setSeckillId(id);
                seckillOrder.setMoney(seckillGoods.getCostPrice());
                seckillOrder.setUserId(username);
                seckillOrder.setCreateTime(new Date());
                seckillOrder.setStatus("0");//未支付状态

                //将秒杀订单存入redis
                redisTemplate.boundHashOps("SeckillOrder").put(username, seckillOrder);

                /**
                 * 创建支付日志，用于保存支付信息
                 */
                PayLog payLog = new PayLog();
                payLog.setOutTradeNo(idWorker.nextId() + "");//订单号
                payLog.setCreateTime(new Date());
                payLog.setTotalFee((long) (seckillOrder.getMoney().doubleValue() * 100));//先乘以100再转为long，
                payLog.setUserId(username);
                payLog.setTradeState("0");//待付款状态
                payLog.setOrderList(seckillOrder.getId() + "");
                payLog.setPayType("2");

                //把支付日志储存到redis
                redisTemplate.boundHashOps("payLog").put(username, payLog);

                /**
                 * 抢单成功，更新抢单状态
                 */
                seckillStatus.setStatus(2);//秒杀等待支付
                seckillStatus.setOrderId(seckillOrder.getId());
                seckillStatus.setMoney(seckillOrder.getMoney().floatValue());

                redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);

                /**
                 * 监听当前订单的支付状态（线程等待）
                 */
                monitotrStatusTsak.monitorPayStatus(username, seckillOrder);

                /**
                 * 同步库存到MySQL
                 */
                //库存减一
                seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
                //判断还有没有库存
                if (seckillGoods.getStockCount() <= 0) {
                    //没有库存，把商品数据同步到MySQL
                    seckillGoodsMapper.updateById(seckillGoods);
                    //清空redis中该商品信息
                    redisTemplate.boundHashOps("SeckillGoods_" + time).delete(id);
                } else {
                    //还有库存，更新reids商品库存
                    redisTemplate.boundHashOps("SeckillGoods_" + time).put(id, seckillGoods);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //释放锁
                redisTemplate.delete(goodsId);
            }

        }

    }

}
