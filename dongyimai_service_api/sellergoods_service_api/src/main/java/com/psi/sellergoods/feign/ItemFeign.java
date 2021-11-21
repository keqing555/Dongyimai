package com.psi.sellergoods.feign;

import com.psi.entity.Result;
import com.psi.sellergoods.pojo.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("dym-sellergoods")
@RequestMapping("/item")
public interface ItemFeign {

    /***
     * 根据审核状态查询Sku，0，1
     * @param status
     * @return
     */
    @GetMapping("status/{status}")
    Result<List<Item>> findByStatus(@PathVariable("status") String status);

    /**
     * 根据id查询Item
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Item> findById(@PathVariable("id") Long id);

    /***
     * 根据订单量减少库存
     * @param username
     * @return
     */
    @PostMapping("reduceCount")
    Result reduceCount(@RequestParam("username") String username);
}
