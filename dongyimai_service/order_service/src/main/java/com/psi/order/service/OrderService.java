package com.psi.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psi.entity.PageResult;
import com.psi.order.pojo.Order;
import com.psi.order.pojo.PayLog;


import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:Order业务层接口
 * @Date 2021/2/1 14:19
 *****/

public interface OrderService extends IService<Order> {

    /***
     * Order多条件分页查询
     * @param order
     * @param page
     * @param size
     * @return
     */
    PageResult<Order> findPage(Order order, int page, int size);

    /***
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    PageResult<Order> findPage(int page, int size);

    /***
     * Order多条件搜索方法
     * @param order
     * @return
     */
    List<Order> findList(Order order);

    /***
     * 删除Order
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Order数据
     * @param order
     */
    void update(Order order);

    /***
     * 新增Order
     * @param map
     */
    void add(Map map);

    /**
     * 根据ID查询Order
     *
     * @param id
     * @return
     */
    Order findById(Long id);

    /***
     * 查询所有Order
     * @return
     */
    List<Order> findAll();

    /***
     * 根据用户名查询支付日志
     * @param userId
     * @return
     */
    PayLog getPayLogFromRedis(String userId);
}
