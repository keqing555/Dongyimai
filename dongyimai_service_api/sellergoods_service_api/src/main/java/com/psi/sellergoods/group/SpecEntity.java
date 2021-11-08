package com.psi.sellergoods.group;

import com.psi.sellergoods.pojo.Specification;
import com.psi.sellergoods.pojo.SpecificationOption;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "规格组合实体类", value = "SpecEntity")
public class SpecEntity implements Serializable {

    @ApiModelProperty(value = "规格对象", required = false)
    private Specification specification;

    @ApiModelProperty(value = "规格选项对象", required = false)
    private List<SpecificationOption> specificationOptionList;

    /**
     * get set 方法
     */
    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public List<SpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<SpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
