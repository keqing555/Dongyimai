package com.psi.item.feign;

import com.psi.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "dym-itempage")
@RequestMapping("/page")
public interface PageFeign {
    /***
     * 根据spuId生成静态页面
     * @param id
     * @return
     */
    @GetMapping("createHtml/{id}")
    Result createHtml(@PathVariable("id") Long id);
}
