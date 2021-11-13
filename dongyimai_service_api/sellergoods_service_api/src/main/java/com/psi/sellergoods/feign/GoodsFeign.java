package com.psi.sellergoods.feign;

import com.psi.entity.Result;
import com.psi.sellergoods.group.GoodsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "dym-sellergoods")
@RequestMapping("/goods")
public interface GoodsFeign {
    /***
     * 根据id查询spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<GoodsEntity> findById(@PathVariable("id") Long id);
}
