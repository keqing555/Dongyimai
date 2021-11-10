package com.psi.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Document(indexName = "skuinfo")
public class SkuInfo implements Serializable {
    //商品id，同时也是商品编号
    @Id
    private Long id;

    //SKU名称 该类中title字段的值存储到es中，es中的字段和属性名默认一致的。
    //并且es中该字段是文本类型，使用ik_smart模式进行分词检索 （Text+分词模式）
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String title;

    //商品价格,es中以Double类型存储
    @Field(type = FieldType.Double)
    private BigDecimal price;

    //分类名称 ，在es中存储该字段以keyword类型存储
    //该类型不可分词，作为一个完整短语存在
    @Field(type = FieldType.Keyword)
    private String category;

    //品牌名称，完整单词，不会进行分词检索
    @Field(type = FieldType.Keyword)
    private String brand;

    //库存数量
    private Integer num;
    //商品图片
    private String image;
    //商品状态，1-正常，2-下架，3-删除
    private String status;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //是否默认
    private String isDefault;
    //goodsId
    private Long goodsId;
    //类目ID
    private Long categoryId;

    //规格
    private String spec;

    //规格参数（定义的一个额外的属性，在Item中没有，将字符串spec，转成json，赋值给specMap）
    private Map<String, Object> specMap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Map<String, Object> getSpecMap() {
        return specMap;
    }

    public void setSpecMap(Map<String, Object> specMap) {
        this.specMap = specMap;
    }
}