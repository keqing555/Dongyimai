package com.psi.page.service;

public interface PageService {
    /***
     * 根据商品id生成静态页面
     * @param spuId
     */
    public void createHtmlPage(Long spuId);
}
