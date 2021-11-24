package com.psi.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psi.entity.PageResult;
import com.psi.seckill.bean.SeckillStatus;
import com.psi.seckill.pojo.SeckillOrder;

import java.util.List;

/****
 * @Author:ujiuye
 * @Description:SeckillOrder业务层接口
 * @Date 2021/2/1 14:19
 *****/

public interface SeckillOrderService extends IService<SeckillOrder> {

    /***
     * SeckillOrder多条件分页查询
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    PageResult<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size);

    /***
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    PageResult<SeckillOrder> findPage(int page, int size);

    /***
     * SeckillOrder多条件搜索方法
     * @param seckillOrder
     * @return
     */
    List<SeckillOrder> findList(SeckillOrder seckillOrder);

    /***
     * 删除SeckillOrder
     * @param id
     */
    void delete(Long id);

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     */
    void update(SeckillOrder seckillOrder);

    /***
     * 新增SeckillOrder
     * @param seckillOrder
     */
    void add(SeckillOrder seckillOrder);

    /**
     * 根据ID查询SeckillOrder
     *
     * @param id
     * @return
     */
    SeckillOrder findById(Long id);

    /***
     * 查询所有SeckillOrder
     * @return
     */
    List<SeckillOrder> findAll();

    /***
     * 添加秒杀订单
     * @param id
     * @param time
     * @param username
     * @return
     */
    Boolean addSeckillOrder(long id, String time, String username);

    /***
     * 查询抢单状态
     * @param username
     * @return
     */
    SeckillStatus queryStatus(String username);
}
