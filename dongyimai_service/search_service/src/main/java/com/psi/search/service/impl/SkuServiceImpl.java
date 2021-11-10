package com.psi.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.psi.entity.Result;
import com.psi.search.dao.SkuEsMapper;
import com.psi.search.pojo.SkuInfo;
import com.psi.search.service.SkuService;
import com.psi.sellergoods.feign.ItemFeign;
import com.psi.sellergoods.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuEsMapper skuEsMapper;
    @Autowired
    private ItemFeign itemFeign;

    @Override
    public void importSku() {
        //调用商品微服务，获取sku数据
        Result<List<Item>> result = itemFeign.findByStatus("1");
        List<Item> itemList = result.getData();
        //把itemList转换为List<SkuInfo>
        String str = JSON.toJSONString(itemList);
        List<SkuInfo> skuInfoList = JSON.parseArray(str, SkuInfo.class);
        //遍历sku集合
        for (SkuInfo skuInfo : skuInfoList) {
            //获取规格specMap
            Map<String, Object> specMap = JSON.parseObject(skuInfo.getSpec());
            //关联设置到specMap
            skuInfo.setSpecMap(specMap);
        }
        //保存sku集合到ES里
        skuEsMapper.saveAll(skuInfoList);
    }
}
