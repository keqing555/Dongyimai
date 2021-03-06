package com.psi.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psi.entity.PageResult;
import com.psi.seckill.pojo.SeckillGoods;

import java.util.List;

/****
 * @Author:ujiuye
 * @Description:SeckillGoods业务层接口
 * @Date 2021/2/1 14:19
 *****/

public interface SeckillGoodsService extends IService<SeckillGoods> {

    /***
     * SeckillGoods多条件分页查询
     * @param seckillGoods
     * @param page
     * @param size
     * @return
     */
    PageResult<SeckillGoods> findPage(SeckillGoods seckillGoods, int page, int size);

    /***
     * SeckillGoods分页查询
     * @param page
     * @param size
     * @return
     */
    PageResult<SeckillGoods> findPage(int page, int size);

    /***
     * SeckillGoods多条件搜索方法
     * @param seckillGoods
     * @return
     */
    List<SeckillGoods> findList(SeckillGoods seckillGoods);

    /***
     * 删除SeckillGoods
     * @param id
     */
    void delete(Long id);

    /***
     * 修改SeckillGoods数据
     * @param seckillGoods
     */
    void update(SeckillGoods seckillGoods);

    /***
     * 新增SeckillGoods
     * @param seckillGoods
     */
    void add(SeckillGoods seckillGoods);

    /**
     * 根据ID查询SeckillGoods
     *
     * @param id
     * @return
     */
    SeckillGoods findById(Long id);

    /***
     * 查询所有SeckillGoods
     * @return
     */
    List<SeckillGoods> findAll();

    /***
     * 获取指定时间对应的商品列表
     * @param date
     * @return
     */
    List<SeckillGoods> findSeckillGoodsByDate(String date);

    /***
     * 根据id查询商品
     * @param time
     * @param id
     * @return
     */
    SeckillGoods findSeckilGoodsById(String time, long id);



}
