package com.psi.sellergoods.service;

import com.psi.entity.PageResult;
import com.psi.sellergoods.pojo.Brand;

import java.util.List;

public interface BrandService {
    //获取所有品牌
    List<Brand> getAllBrands();

    //根据id获取品牌
    Brand getBrandById(Long id);

    //增加品牌
    void addBrand(Brand brand);

    //修改品牌信息
    void updateBrand(Brand brand);

    //删除
    void deleteBrand(Long id);

    //多条件搜索品牌
    List<Brand> getBrandsByConditions(Brand brand);

    //品牌列表分页查询
    PageResult<Brand> getBrandPage(int index, int size);

    //多条件分页查询
    PageResult<Brand> getBrandPage(Brand brand, int index, int page);
}
