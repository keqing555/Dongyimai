package com.psi.sellergoods.dao;

import com.psi.sellergoods.pojo.Brand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:Brand的Dao
 * @Date 2021/2/1 14:19
 *****/
public interface BrandMapper extends BaseMapper<Brand> {

    @Select("select id, name as text from tb_brand")
    public List<Map> selectOptions();
}
