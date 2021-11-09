package com.psi.sellergoods.service.impl;

import com.alibaba.fastjson.JSON;
import com.psi.sellergoods.dao.*;
import com.psi.sellergoods.group.GoodsEntity;
import com.psi.sellergoods.pojo.*;
import com.psi.sellergoods.service.GoodsService;
import com.psi.entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:Goods业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
@Transactional  //开启事务
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Autowired
    private BrandMapper brandMapper;

    /**
     * Goods条件+分页查询
     *
     * @param goods 查询条件
     * @param page  页码
     * @param size  页大小
     * @return 分页结果
     */
    @Override
    public PageResult<Goods> findPage(Goods goods, int page, int size) {
        Page<Goods> mypage = new Page<>(page, size);
        QueryWrapper<Goods> queryWrapper = this.createQueryWrapper(goods);
        IPage<Goods> iPage = this.page(mypage, queryWrapper);
        return new PageResult<Goods>(iPage.getTotal(), iPage.getRecords());
    }

    /**
     * Goods分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<Goods> findPage(int page, int size) {
        Page<Goods> mypage = new Page<>(page, size);
        IPage<Goods> iPage = this.page(mypage, new QueryWrapper<Goods>());

        return new PageResult<Goods>(iPage.getTotal(), iPage.getRecords());
    }

    /**
     * Goods条件查询
     *
     * @param goods
     * @return
     */
    @Override
    public List<Goods> findList(Goods goods) {
        //构建查询条件
        QueryWrapper<Goods> queryWrapper = this.createQueryWrapper(goods);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * Goods构建查询对象
     *
     * @param goods
     * @return
     */
    public QueryWrapper<Goods> createQueryWrapper(Goods goods) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (goods != null) {
            // 主键
            if (!StringUtils.isEmpty(goods.getId())) {
                queryWrapper.eq("id", goods.getId());
            }
            // 商家ID
            if (!StringUtils.isEmpty(goods.getSellerId())) {
                queryWrapper.eq("seller_id", goods.getSellerId());
            }
            // SPU名
            if (!StringUtils.isEmpty(goods.getGoodsName())) {
                queryWrapper.eq("goods_name", goods.getGoodsName());
            }
            // 默认SKU
            if (!StringUtils.isEmpty(goods.getDefaultItemId())) {
                queryWrapper.eq("default_item_id", goods.getDefaultItemId());
            }
            // 状态
            if (!StringUtils.isEmpty(goods.getAuditStatus())) {
                queryWrapper.eq("audit_status", goods.getAuditStatus());
            }
            // 是否上架
            if (!StringUtils.isEmpty(goods.getIsMarketable())) {
                queryWrapper.eq("is_marketable", goods.getIsMarketable());
            }
            // 品牌
            if (!StringUtils.isEmpty(goods.getBrandId())) {
                queryWrapper.eq("brand_id", goods.getBrandId());
            }
            // 副标题
            if (!StringUtils.isEmpty(goods.getCaption())) {
                queryWrapper.eq("caption", goods.getCaption());
            }
            // 一级类目
            if (!StringUtils.isEmpty(goods.getCategory1Id())) {
                queryWrapper.eq("category1_id", goods.getCategory1Id());
            }
            // 二级类目
            if (!StringUtils.isEmpty(goods.getCategory2Id())) {
                queryWrapper.eq("category2_id", goods.getCategory2Id());
            }
            // 三级类目
            if (!StringUtils.isEmpty(goods.getCategory3Id())) {
                queryWrapper.eq("category3_id", goods.getCategory3Id());
            }
            // 小图
            if (!StringUtils.isEmpty(goods.getSmallPic())) {
                queryWrapper.eq("small_pic", goods.getSmallPic());
            }
            // 商城价
            if (!StringUtils.isEmpty(goods.getPrice())) {
                queryWrapper.eq("price", goods.getPrice());
            }
            // 分类模板ID
            if (!StringUtils.isEmpty(goods.getTypeTemplateId())) {
                queryWrapper.eq("type_template_id", goods.getTypeTemplateId());
            }
            // 是否启用规格
            if (!StringUtils.isEmpty(goods.getIsEnableSpec())) {
                queryWrapper.eq("is_enable_spec", goods.getIsEnableSpec());
            }
            // 是否删除
            if (!StringUtils.isEmpty(goods.getIsDelete())) {
                queryWrapper.eq("is_delete", goods.getIsDelete());
            }
        }
        return queryWrapper;
    }

    /**
     * 逻辑删除
     * 必须先下架才能删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Goods goods = this.getById(id);
        if (goods == null)
            throw new RuntimeException("未查询到要删除的商品");
        if (goods.getIsMarketable().equals("0"))
            throw new RuntimeException("不能删除未下架的商品！");
        //逻辑删除
        goods.setIsDelete("1");
        //设置未审核
        goods.setAuditStatus("0");
        this.update(goods);
    }

    /**
     * 修改Goods
     *
     * @param goods
     */
    @Override
    public void update(Goods goods) {
        this.updateById(goods);
    }

    /**
     * 增加Goods
     *
     * @param goodsEntity
     */
    @Override
    public void add(GoodsEntity goodsEntity) {
        //把商品设置为未审核状态
        goodsEntity.getGoods().setAuditStatus("0");
        //保存SPU商品信息对象
        this.save(goodsEntity.getGoods());
        //获取商品对象主键，给商品扩展信息对象设置主键
        goodsEntity.getGoodsDesc().setGoodsId(goodsEntity.getGoods().getId());
        //保存商品扩展信息
        goodsDescMapper.insert(goodsEntity.getGoodsDesc());

        //保存SKU，是否启用规格
        if ("1".equals(goodsEntity.getGoods().getIsEnableSpec())) {
            //保存SKU
            if (!CollectionUtils.isEmpty(goodsEntity.getItemList())) {
                for (Item item : goodsEntity.getItemList()) {
                    String title = goodsEntity.getGoods().getGoodsName();
                    //设置SKU的名称  商品名+规格名
                    Map<String, String> specMap = JSON.parseObject(item.getSpec(), Map.class);
                    for (String key : specMap.keySet()) {
                        title += specMap.get(key) + "";
                    }
                    item.setTitle(title);//SKU名称
                    item.setCategoryId(goodsEntity.getGoods().getCategory3Id());//获取商品三级分类id
                    item.setCreateTime(new Date());//创建时间
                    item.setUpdateTime(new Date());//更新时间
                    item.setGoodsId(goodsEntity.getGoods().getId());//SPU_ID
                    item.setSellerId(goodsEntity.getGoods().getSellerId());//商家id
                    //查询分类对象
                    ItemCat itemCat = itemCatMapper.selectById(goodsEntity.getGoods().getCategory3Id());
                    item.setCategory(itemCat.getName());//分类名称
                    //查询品牌对象
                    Brand brand = brandMapper.selectById(goodsEntity.getGoods().getBrandId());
                    item.setBrand(brand.getName());//品牌名称
                    List<Map> imageList = JSON.parseArray(goodsEntity.getGoodsDesc().getItemImages(), Map.class);
                    if (!CollectionUtils.isEmpty(imageList)) {
                        item.setImage((String) (imageList.get(0).get("url")));//商品图片
                    }
                    //保存SKU信息
                    itemMapper.insert(item);
                }
            }
        } else {
            //不启用规格，SKU为默认值
            Item item = new Item();
            item.setTitle(goodsEntity.getGoods().getGoodsName());     //商品名称
            item.setPrice(goodsEntity.getGoods().getPrice());      //默认使用SPU的价格
            item.setNum(9999);
            item.setStatus("1");            //是否启用
            item.setIsDefault("1");         //是否默认
            item.setSpec("{}");             //没有选择规格，则放置空JSON结构
            item.setGoodsId(goodsEntity.getGoods().getId());
            itemMapper.insert(item);
        }
    }


    /**
     * 根据ID查询Goods
     *
     * @param id
     * @return
     */
    @Override
    public GoodsEntity findById(Long id) {
        //根据ID查询SPU信息
        Goods goods = goodsMapper.selectById(id);
        if (goods.getIsDelete().equals("1"))
            throw new RuntimeException("该商品已被删除");
        //根据id查询商品扩展信息
        GoodsDesc goodsDesc = goodsDescMapper.selectById(id);
        //根据id查询SKU
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", id);
        List<Item> items = itemMapper.selectList(queryWrapper);
        //生成组合实体对象
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoods(goods);
        goodsEntity.setGoodsDesc(goodsDesc);
        goodsEntity.setItemList(items);
        return goodsEntity;
    }

    /**
     * 查询Goods全部数据
     *
     * @return
     */
    @Override
    public List<Goods> findAll() {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", "0");
        return goodsMapper.selectList(queryWrapper);
    }

    @Override
    public void audit(Long id) {
        //审核通过自动上架
        Goods goods = goodsMapper.selectById(id);
        if (goods == null)
            throw new RuntimeException("商品不存在");
        if (goods.getIsDelete().equals("1"))
            throw new RuntimeException("该商品已删除");
        //审核通过
        goods.setAuditStatus("1");
        //上架
        goods.setIsMarketable("1");
        goodsMapper.updateById(goods);
    }

    @Override
    public void push(Long id) {
        Goods goods = goodsMapper.selectById(id);
        if (goods == null)
            throw new RuntimeException("该商品不存在");
        if (goods.getIsDelete().equals("1"))
            throw new RuntimeException("该商品已被删除");
        if (goods.getAuditStatus().equals("0"))
            throw new RuntimeException("该商品审核未通过");
        //上架商品
        goods.setIsMarketable("1");
        goodsMapper.updateById(goods);
    }

    @Override
    public void pull(Long id) {
        Goods goods = goodsMapper.selectById(id);
        if (goods == null)
            throw new RuntimeException("该商品不存在");
        if (goods.getIsDelete().equals("1"))
            throw new RuntimeException("该商品已被删除");
        //下架商品
        goods.setIsMarketable("0");
        goodsMapper.updateById(goods);
    }

    @Override
    public int pushMany(Long[] ids) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", Arrays.asList(ids));
        queryWrapper.eq("is_marketable", "0");//下架的商品
        queryWrapper.eq("audit_status", "1");//审核通过的商品
        queryWrapper.eq("is_delete", "0");//未被删除的商品
        //设置上架数据模型
        Goods goods = new Goods();
        goods.setIsMarketable("1");
        int rows = goodsMapper.update(goods, queryWrapper);
        return rows;
    }

    @Override
    public int pullMany(Long[] ids) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", Arrays.asList(ids));
        queryWrapper.eq("is_marketable", "1");//上架的商品
        queryWrapper.eq("is_delete", "0");//未被删除的商品
        //设置下架数据模型
        Goods goods = new Goods();
        goods.setIsMarketable("0");
        int rows = goodsMapper.update(goods, queryWrapper);
        return rows;
    }
}
