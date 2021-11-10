package com.psi.search.dao;

import com.psi.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 本接口用于索引数据操作
 * 将数据导入ES索引库中
 */
@Repository
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo, Long> {

}
