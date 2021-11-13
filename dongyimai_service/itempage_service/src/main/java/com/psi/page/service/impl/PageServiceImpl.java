package com.psi.page.service.impl;

import com.alibaba.fastjson.JSON;
import com.psi.entity.Result;
import com.psi.page.service.PageService;
import com.psi.sellergoods.feign.GoodsFeign;
import com.psi.sellergoods.feign.ItemCatFeign;
import com.psi.sellergoods.group.GoodsEntity;
import com.psi.sellergoods.pojo.Goods;
import com.psi.sellergoods.pojo.GoodsDesc;
import com.psi.sellergoods.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private GoodsFeign goodsFeign;
    @Autowired
    private ItemCatFeign itemCatFeign;
    @Autowired
    private TemplateEngine templateEngine;

    //静态文件的生成路径
    @Value("${pagepath}")
    private String pagepath;


    /***
     * 生成静态页面
     * @param spuId
     */
    @Override
    public void createHtmlPage(Long spuId) {
        //获取上下文对象
        Context context = new Context();
        Map<String, Object> dataMap = buildDateModel(spuId);
        context.setVariables(dataMap);

        //设置路径
        File path = new File(pagepath);
        if (!path.exists()) {
            path.mkdirs();
        }

        //生成文件
        File file = new File(path, spuId + ".html");

        //生成页面
        try {
            PrintWriter pw = new PrintWriter(file, "UTF-8");
            //templates/item.html  模板
            templateEngine.process("item", context, pw);
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /***
     * 构建数据模型
     * @param spuId
     * @return
     */
    private Map<String, Object> buildDateModel(Long spuId) {
        Map<String, Object> dataMap = new HashMap<>();
        //获取spu和sku
//        GoodsEntity goodsEntity = goodsFeign.findById(spuId).getData();
        Result result = goodsFeign.findById(spuId);
        GoodsEntity goodsEntity = (GoodsEntity) result.getData();
        Goods goods = goodsEntity.getGoods();
        GoodsDesc goodsDesc = goodsEntity.getGoodsDesc();
        List<Item> itemList = goodsEntity.getItemList();

        //添加数据到map集合
        dataMap.put("goods", goods);
        dataMap.put("goodsDesc", goodsDesc);
        dataMap.put("itemList", itemList);
        dataMap.put("specificationList",
                JSON.parseArray(goodsDesc.getSpecificationItems(), Map.class));
        dataMap.put("imageList",
                JSON.parseArray(goodsDesc.getItemImages(), Map.class));

        //添加分类数据
        dataMap.put("category1", itemCatFeign.findById(goods.getCategory1Id()).getData());
        dataMap.put("category2", itemCatFeign.findById(goods.getCategory2Id()).getData());
        dataMap.put("category3", itemCatFeign.findById(goods.getCategory3Id()).getData());

        return dataMap;
    }
}
