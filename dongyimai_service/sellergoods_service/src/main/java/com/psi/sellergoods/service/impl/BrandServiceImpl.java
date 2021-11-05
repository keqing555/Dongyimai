package com.psi.sellergoods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psi.sellergoods.dao.BrandMapper;
import com.psi.sellergoods.pojo.Brand;
import com.psi.sellergoods.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Override
    public List<Brand> getAllBrands() {
        return this.list();
    }

    @Override
    public Brand getBrandById(Long id) {
        return this.getById(id);
    }

    @Override
    public void addBrand(Brand brand) {
        //参数校验
        if (brand == null)
            throw new RuntimeException("参数不能为空！");
        if (StringUtils.isEmpty(brand.getName()))
            throw new RuntimeException("品牌名称不能为空！");
        if (StringUtils.isEmpty(brand.getFirstChar()))
            throw new RuntimeException("首字母不能为空！");
        //插入数据
        this.save(brand);
    }
}
