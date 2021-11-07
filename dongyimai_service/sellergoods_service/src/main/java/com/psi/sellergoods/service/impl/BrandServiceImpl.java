package com.psi.sellergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psi.entity.PageResult;
import com.psi.sellergoods.dao.BrandMapper;
import com.psi.sellergoods.pojo.Brand;
import com.psi.sellergoods.service.BrandService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public void updateBrand(Brand brand) {
        //主键校验
        if (brand == null || StringUtils.isEmpty(brand.getId()))
            throw new RuntimeException("没有主键！");
        //修改
        this.updateById(brand);
    }

    @Override
    public void deleteBrand(Long id) {
        if (id <= 0)
            throw new RuntimeException("请输入正确的ID");
        this.removeById(id);
    }

    /**
     * 封装多条件查询条件
     *
     * @param brand
     * @return queryWrapper
     */
    private QueryWrapper<Brand> queryConditions(Brand brand) {
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        if (brand != null) {
            if (!StringUtils.isEmpty(brand.getId()))
                //根据主键查询
                queryWrapper.eq("id", brand.getId());
            if (!StringUtils.isEmpty(brand.getName()))
                //根据名称查询
                queryWrapper.like("name", brand.getName());
            if (!StringUtils.isEmpty(brand.getFirstChar()))
                //根据首字母查询
                queryWrapper.eq("first_char", brand.getFirstChar());
        }
        return queryWrapper;
    }

    @Override
    public List<Brand> getBrandsByConditions(Brand brand) {
        //构建查询条件
        QueryWrapper<Brand> queryWrapper = this.queryConditions(brand);
        //根据查询条件查询
        List<Brand> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public PageResult<Brand> getBrandPage(int index, int size) {
        Page<Brand> brandPage = new Page<>(index, size);

        IPage<Brand> iPage = this.page(brandPage, new QueryWrapper<Brand>());

        return new PageResult<Brand>(iPage.getTotal(), iPage.getRecords());
    }

    @Override
    public PageResult<Brand> getBrandPage(Brand brand, int index, int page) {
        Page<Brand> brandPage = new Page<>(index, page);
        QueryWrapper<Brand> queryWrapper = this.queryConditions(brand);
        IPage<Brand> iPage = this.page(brandPage, queryWrapper);
        return new PageResult<>(iPage.getTotal(), iPage.getRecords());
    }
}
