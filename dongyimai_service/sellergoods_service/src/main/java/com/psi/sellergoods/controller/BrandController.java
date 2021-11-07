package com.psi.sellergoods.controller;

import com.psi.entity.PageResult;
import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.sellergoods.pojo.Brand;
import com.psi.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/brand")
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
     * 添加品牌
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

    /**
     * 根据id修改品牌
     *
     * @param brand
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result updateBrand(@RequestBody Brand brand,
                              @PathVariable("id") Long id) {
        try {
            //设置id
            brand.setId(id);
            //修改
            brandService.updateBrand(brand);
            return new Result(true, StatusCode.OK, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "修改失败：" + e.getMessage());
        }
    }

    /**
     * 根据id删除品牌
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteBrand(@PathVariable("id") Long id) {
        try {
            brandService.deleteBrand(id);
            return new Result(true, StatusCode.OK, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "删除失败：" + e.getMessage());
        }
    }

    /**
     * 多条件查询品牌
     *
     * @param brand
     * @return
     */
    @PostMapping("/search")
    public Result getBrandByConditions(@RequestBody(required = false) Brand brand) {
        List<Brand> list = brandService.getBrandsByConditions(brand);
        if (list == null && list.size() <= 0)
            return new Result(false, StatusCode.ERROR, "未查询到商品");
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 品牌分页
     * 须创建分页插件配置类 MyBatisPlusInterceptor
     *
     * @param index 当前页
     * @param size  每页条数
     * @return
     */
    @GetMapping("page/{index}/{size}")
    public Result<PageResult<Brand>> getBrandPage(@PathVariable("index") int index,
                                                  @PathVariable("size") int size) {
        PageResult<Brand> brandPage = brandService.getBrandPage(index, size);
        return new Result<>(true, StatusCode.OK, "品牌分页", brandPage);
    }

    /**
     * 多条件分页查询
     *
     * @param brand
     * @param index
     * @param size
     * @return
     */
    @PostMapping("page/{index}/{size}")
    public PageResult<Brand> getBrandPage(@RequestBody Brand brand,
                                          @PathVariable("index") int index,
                                          @PathVariable("size") int size) {
        return brandService.getBrandPage(brand, index, size);
    }
}
