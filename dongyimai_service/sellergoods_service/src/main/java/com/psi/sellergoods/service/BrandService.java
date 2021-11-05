package com.psi.sellergoods.service;

import com.psi.sellergoods.pojo.Brand;

import java.util.List;

public interface BrandService {
    //获取所有品牌
    List<Brand> getAllBrands();

    //根据id获取品牌
    Brand getBrandById(Long id);

    //增加品牌
    void addBrand(Brand brand);
}
