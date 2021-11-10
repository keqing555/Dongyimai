package com.psi.sellergoods.feign;

import com.psi.entity.Result;
import com.psi.sellergoods.pojo.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("dym-sellergoods")
@RequestMapping("/item")
public interface ItemFeign {

    /***
     * 根据审核状态查询Sku
     * @param status
     * @return
     */
    @GetMapping("status/{status}")
    Result<List<Item>> findByStatus(@PathVariable("status") String status);
}
