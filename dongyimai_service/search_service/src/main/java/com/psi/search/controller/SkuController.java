package com.psi.search.controller;

import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin    //允许跨域
public class SkuController {
    @Autowired
    private SkuService skuService;

    /**
     * 导入sku到ES
     *
     * @return
     */
    @PutMapping
    public Result importSku() {
        skuService.importSku();
        return new Result(true, StatusCode.OK, "导入SKU到ES中成功！");
    }

    /***
     * 清空ES_sku
     * @return
     */
    @DeleteMapping
    public Result clearSku() {
        try {
            skuService.clearSku();
            return new Result(true, StatusCode.OK, "清空ES_sku成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "清空失败！");
        }

    }

    @PostMapping
    public Map search(@RequestBody(required = false) Map searchMap) {
        return skuService.search(searchMap);
    }
}
