package com.psi.seckill.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.psi.seckill.dao.SeckillGoodsMapper;
import com.psi.seckill.pojo.SeckillGoods;
import com.psi.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 定时添加秒杀商品任务类
 */
@Component
public class SeckillGoodsPushTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    //定时任务，每30秒执行一次
    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushToRedis() {
        System.out.println("执行定时任务");
        //获得时间段集合
        List<Date> dateMenus = DateUtil.getDateMenus();

        for (Date startDate : dateMenus) {
            //提取开始时间，转换为年月日格式字符串
            String extName = DateUtil.date2Str(startDate);
            //创建查询条件对象
            QueryWrapper<SeckillGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", "1");//审核通过的商品
            queryWrapper.gt("stock_count", 0);//库存大于0
            queryWrapper.ge("start_time", DateUtil.date2StrFull(startDate));//活动开始时间大于等于时间段起始时间
            queryWrapper.le("end_time", DateUtil.date2StrFull(DateUtil.addDateHour(startDate, 2)));

            //读取redis里当天的秒杀商品
            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + extName).keys();

            //redis里已经有的商品，排除
            if (keys != null && keys.size() > 0) {
                queryWrapper.notIn("id", keys);
            }

            //查询符合条件的数据
            List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectList(queryWrapper);

            //把符合条件的商品储存到redis，一个时间段对应一个hash
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                redisTemplate.boundHashOps("SeckillGoods_" + extName).put(seckillGoods.getId(), seckillGoods);
                //设置超时时间为两小时
                redisTemplate.expireAt("SeckillGoods_" + extName, DateUtil.addDateHour(startDate, 2));
            }
        }
    }
}
