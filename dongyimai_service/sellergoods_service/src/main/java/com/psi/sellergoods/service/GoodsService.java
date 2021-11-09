package com.psi.sellergoods.service;

import com.psi.sellergoods.group.GoodsEntity;
import com.psi.sellergoods.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.psi.entity.PageResult;

import java.util.List;

/****
 * @Author:ujiuye
 * @Description:Goods业务层接口
 * @Date 2021/2/1 14:19
 *****/

public interface GoodsService extends IService<Goods> {

    /***
     * Goods多条件分页查询
     * @param goods
     * @param page
     * @param size
     * @return
     */
    PageResult<Goods> findPage(Goods goods, int page, int size);

    /***
     * Goods分页查询
     * @param page
     * @param size
     * @return
     */
    PageResult<Goods> findPage(int page, int size);

    /***
     * Goods多条件搜索方法
     * @param goods
     * @return
     */
    List<Goods> findList(Goods goods);

    /***
     * 删除Goods
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Goods数据
     * @param goods
     */
    void update(Goods goods);

    /***
     * 新增Goods
     * @param goodsEntity
     */
    // void add(Goods goods);
    void add(GoodsEntity goodsEntity);

    /***
     * 根据ID查询Goods
     * @param id
     * @return
     */
    GoodsEntity findById(Long id);

    /***
     * 查询所有Goods
     * @return
     */
    List<Goods> findAll();

    /***
     * 商品审核，审核通过自动上架
     * @param id
     */
    void audit(Long id);

    /***
     * 商品上架
     * @param id
     */
    void push(Long id);

    /***
     * 商品下架
     * @param id
     */
    void pull(Long id);

    /***
     * 批量上架
     * @param ids
     * @return
     */
    int pushMany(Long[] ids);

    /***
     * 批量下架
     * @param ids
     * @return
     */
    int pullMany(Long[] ids);
}
