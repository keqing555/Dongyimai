package com.psi.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.psi.entity.Result;
import com.psi.search.dao.SkuEsMapper;
import com.psi.search.pojo.SkuInfo;
import com.psi.search.service.SkuService;
import com.psi.sellergoods.feign.ItemFeign;
import com.psi.sellergoods.pojo.Item;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
            keywords = "小米";//给一个默认值
        }
        /***
         * 统计查询
         */
        //创建查询对象的构建对象NativeSearchQueryBuilder
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        /***
         * 设置查询过滤
         */

        //创建多条件组合查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //设置品牌查询条件
        if (!StringUtils.isEmpty(searchMap.get("brand"))) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("brand", searchMap.get("brand")));
        }
        //设置分类查询条件
        if (!StringUtils.isEmpty(searchMap.get("category"))) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("category", searchMap.get("category")));
        }
        //设置规格过滤查询
        for (String key : searchMap.keySet()) {
            if (key.startsWith("spec_")) {
                String specName = key.substring(5);//截取规格名称
                String specValue = searchMap.get(key);//获取规格值
                //termQuery完全匹配搜索
                boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + specName + ".keyword", specValue));
            }
        }
        //按价格区间过滤 "price":"120-300"
        if (!StringUtils.isEmpty(searchMap.get("price"))) {
            String price = searchMap.get("price");
            String[] split = price.split("-");
            //设置价格区间
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(split[1]));
        }


        //关联过滤查询对象到查询器
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);


        /***
         *   fieldName.keyword决定是否采用es分词数据源，
         *  不带keyword即查询text格式数据（分词），
         *  带即查询keyword格式数据（不分词）
         */

        /***
         * 设置类别分组
         * terms表示分组后的列名
         */
        nativeSearchQueryBuilder.addAggregation(
                AggregationBuilders.terms("skuCategoryGroup")
                        .field("category").size(10));
        /***
         * 设置品牌分组
         */
        nativeSearchQueryBuilder.addAggregation(
                AggregationBuilders.terms("skuBrandGroup")
                        .field("brand"));
        /***
         * 设置规格分组
         */
        nativeSearchQueryBuilder.addAggregation(
                AggregationBuilders.terms("skuSpecGroup")
                        .field("spec.keyword"));

        /**
         * 排序
         */
        //构建排序条件
        String sortRule = searchMap.get("sortRule");
        String sortField = searchMap.get("sortField");//desc asc
        //判断二者都不为空
        if (!StringUtils.isEmpty(sortRule) && !StringUtils.isEmpty(sortField)) {
            //设置排序规则
            SortOrder sortOrder = sortRule.equalsIgnoreCase("DESC") ? SortOrder.DESC : SortOrder.ASC;
            FieldSortBuilder sortBuilder = SortBuilders.fieldSort(sortField).order(sortOrder);
            //关联排序设置
            nativeSearchQueryBuilder.withSort(sortBuilder);
        }


        /***
         * 分页
         */
        Integer pageNum = 1;//默认第一页
        if (!StringUtils.isEmpty(searchMap.get("pageNum"))) {
            pageNum = Integer.parseInt(searchMap.get("pageNum"));//获取当前页码
        }
        Integer pageSize = 20;      //每页显示记录数
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);//第一个参数从0开始
        //关联分页设置
        nativeSearchQueryBuilder.withPageable(pageRequest);


        /***
         * 查询分页完数据后，把关键字高亮一下
         */
        //设置高亮条件
        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("title");
        nativeSearchQueryBuilder.withHighlightFields(highlightTitle);
        HighlightBuilder highlightBuilder = new HighlightBuilder().preTags("em style=\"color:red\">").postTags("</em>");
        nativeSearchQueryBuilder.withHighlightBuilder(highlightBuilder);
        //设置主关键字查询，修改为多字段的搜索条件
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords, "title", "brand", "category"));


        /***
         * 构建查询器
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
         * 获取高亮结果
         */
        for (SearchHit<SkuInfo> searchHit : searchHits) {
            //获取高亮内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            String title = "";
            if (StringUtils.isEmpty(highlightFields.get("title"))) {
                title = searchHit.getContent().getTitle();
            } else {
                title = highlightFields.get("title").get(0);
            }
            //设置高亮标题
            searchHit.getContent().setTitle(title);
        }


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

