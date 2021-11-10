package com.psi.search.controller;

import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("import")
    public Result importSku() {
        skuService.importSku();
        return new Result(true, StatusCode.OK, "导入SKU到ES中成功！");
    }

}
