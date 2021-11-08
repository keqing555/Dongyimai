package com.psi.sellergoods.dao;
import com.psi.sellergoods.pojo.Specification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:Specification的Dao
 * @Date 2021/2/1 14:19
 *****/
public interface SpecificationMapper extends BaseMapper<Specification> {

    //查询规格下拉列表
    @Select("select id,spec_name as text from tb_specification")
    public List<Map> selectOptions();
}
