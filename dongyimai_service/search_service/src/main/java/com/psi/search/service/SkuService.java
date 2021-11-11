package com.psi.search.service;

import java.util.Map;

public interface SkuService {
    /***
     * 导入sku数据
     */
    void importSku();

    /***
     * 清空es——sku数据
     */
    void clearSku();

    /***
     * Map多条件搜索
     * @param searchMap
     * @return
     */
    Map search(Map<String, String> searchMap);
}
