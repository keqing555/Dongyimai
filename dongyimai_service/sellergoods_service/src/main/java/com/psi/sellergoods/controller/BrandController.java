package com.psi.sellergoods.controller;

import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.sellergoods.pojo.Brand;
import com.psi.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
//@CrossOrigin
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 获取所有品牌
     *
     * @return
     */
    @GetMapping     //不写value默认根路径
    public Result<List<Brand>> getAllBrands() {
        List<Brand> allBrands = brandService.getAllBrands();
        return new Result<>(true, StatusCode.OK, "查询成功", allBrands);
    }

    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Brand> getBrandById(@PathVariable("id") Long id) {
        Brand brand = brandService.getBrandById(id);
        if (brand != null) {
            return new Result<>(true, StatusCode.OK, "查询成功", brand);
        } else {
            return new Result<>(false, StatusCode.ERROR, "未查询到！");
        }
    }

    /**
     * 添加商品
     *
     * @param brand
     * @return
     */
    @PostMapping
    public Result addBrand(@RequestBody Brand brand) {
        try {
            brandService.addBrand(brand);
            return new Result(true, StatusCode.OK, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "添加失败！" + e.getMessage());
        }
    }
}
