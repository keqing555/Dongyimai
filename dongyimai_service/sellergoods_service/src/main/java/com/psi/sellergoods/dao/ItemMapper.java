package com.psi.sellergoods.dao;

import com.psi.order.pojo.OrderItem;
import com.psi.sellergoods.pojo.Item;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/****
 * @Author:ujiuye
 * @Description:Item的Dao
 * @Date 2021/2/1 14:19
 *****/
public interface ItemMapper extends BaseMapper<Item> {
    /***
     * 提交订单后减少库存
     * 同时保证库存量大于等于订单量
     * @param orderItem
     * @return
     */
    @Update("update tb_item set num=num-#{num} where id=#{itemId} and num>=#{num}")
    int reduceCount(OrderItem orderItem);
}
