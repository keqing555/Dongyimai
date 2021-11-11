package com.psi.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.psi.entity.Result;
import com.psi.search.dao.SkuEsMapper;
import com.psi.search.pojo.SkuInfo;
import com.psi.search.service.SkuService;
import com.psi.sellergoods.feign.ItemFeign;
import com.psi.sellergoods.pojo.Item;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuEsMapper skuEsMapper;
    @Autowired
    private ItemFeign itemFeign;
    @Autowired
    private ElasticsearchRestTemplate esRestTemplate;

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

    @Override
    public void clearSku() {
        skuEsMapper.deleteAll();
    }

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    @Override
    public Map search(Map<String, String> searchMap) {
        //获取关键字
        String keywords = searchMap.get("keywords");
        if (StringUtils.isEmpty(keywords)) {
            keywords = "华为";//给一个默认值
        }
        //创建查询对象的构建对象NativeSearchQueryBuilder
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        /***
         * 设置类别分组
         * terms表示分组后的列名
         */
        nativeSearchQueryBuilder.addAggregation(
                AggregationBuilders.terms("skuCategoryGroup")
                        .field("category"));
        /***
         * 设置品牌分组
         */
        nativeSearchQueryBuilder.addAggregation(
                AggregationBuilders.terms("skuBrandGroup")
                        .field("brand.keyword"));
        /***
         * 设置规格分组
         */
        nativeSearchQueryBuilder.addAggregation(
                AggregationBuilders.terms("skuSpecGroup")
                        .field("spec.keyword"));


        /***
         * 设置查询条件
         */
        //分词查询title字段
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", keywords);
        nativeSearchQueryBuilder.withQuery(matchQueryBuilder);
        //构建查询对象
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        //  nativeSearchQuery.setPageable()
        //执行查询
        SearchHits<SkuInfo> searchHits = esRestTemplate.search(nativeSearchQuery, SkuInfo.class);
        //对searchHits进行分页封装,默认0-10
        SearchPage<SkuInfo> skuPage = SearchHitSupport.searchPageFor(searchHits, nativeSearchQuery.getPageable());

        /***
         * 获取分组结果
         */
        //类别分组
        Terms categoryTerm = searchHits.getAggregations().get("skuCategoryGroup");
        //品牌分组
        Terms brandTerm = searchHits.getAggregations().get("skuBrandGroup");
        //规格分组
        Terms specTerm = searchHits.getAggregations().get("skuSpecGroup");
        //

        //遍历取出查询的商品信息
        List<SkuInfo> list = new ArrayList<>();
        for (SearchHit<SkuInfo> searchHit : skuPage.getContent()) {//获取搜索到的数据
            SkuInfo skuInfo = searchHit.getContent();
            list.add(skuInfo);
        }
        //返回结果
        Map map = new HashMap();
        map.put("rows", list);//SkuInfo数据集合，当前页
        map.put("total", searchHits.getTotalHits());//总条数
        map.put("totalPages", skuPage.getTotalPages());//总页数
        map.put("categoryList", getStringList(categoryTerm));//类别名称集合
        map.put("brandList", getStringList(brandTerm));//品牌名称集合
        map.put("specMap", getStringSetMap(specTerm));//规格名称集合

        return map;
    }

    /***
     * 获取分组名称集合
     * @param terms
     * @return
     */
    private List<String> getStringList(Terms terms) {
        List<String> getStringList = new ArrayList<>();
        if (terms != null) {
            for (Terms.Bucket bucket : terms.getBuckets()) {
                String groupName = bucket.getKeyAsString();//分类名称
                getStringList.add(groupName);
            }
        }
        return getStringList;
    }

    /***
     * 获取规格分组集合
     * @param terms
     * @return
     */
    private Map<String, Set<String>> getStringSetMap(Terms terms) {
        Map<String, Set<String>> specMap = new HashMap<>();
        //所有不重复规格
        Set<String> specSet = new HashSet<>();
        if (terms != null) {
            for (Terms.Bucket bucket : terms.getBuckets()) {
                specSet.add(bucket.getKeyAsString());//spec
            }
        }

        for (String specJson : specSet) {
            Map<String, String> map = JSON.parseObject(specJson, Map.class);//每个spec
            for (Map.Entry<String, String> entry : map.entrySet()) {//spec单个属性
                String key = entry.getKey();//属性名
                String value = entry.getValue();//属性值
                //获取当前规格名字对应的规格数据
                Set<String> set = specMap.get(key);
                if (set == null) {
                    set = new HashSet<>();
                }
                //将当前规格加入到set集合里
                set.add(value);
                //将规格数据保存到specMap
                specMap.put(key, set);//保存单属性set
            }
        }
        return specMap;
    }
}

