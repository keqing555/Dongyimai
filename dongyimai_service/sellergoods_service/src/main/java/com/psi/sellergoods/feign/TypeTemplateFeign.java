package com.psi.sellergoods.feign;
import com.psi.entity.PageResult;
import com.psi.entity.Result;
import com.psi.sellergoods.pojo.TypeTemplate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@FeignClient(value="sellergoods")
@RequestMapping("/typeTemplate")
public interface TypeTemplateFeign {

    /***
     * TypeTemplate分页条件搜索实现
     * @param typeTemplate
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}" )
    Result<PageResult<TypeTemplate>> findPage(@RequestBody(required = false) TypeTemplate typeTemplate, @PathVariable  int page, @PathVariable  int size);

    /***
     * TypeTemplate分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}" )
    Result<PageResult<TypeTemplate>> findPage(@PathVariable  int page, @PathVariable  int size);

    /***
     * 多条件搜索品牌数据
     * @param typeTemplate
     * @return
     */
    @PostMapping("/search" )
    Result<List<TypeTemplate>> findList(@RequestBody(required = false) TypeTemplate typeTemplate);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}" )
    Result delete(@PathVariable Long id);

    /***
     * 修改TypeTemplate数据
     * @param typeTemplate
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    Result update(@RequestBody TypeTemplate typeTemplate,@PathVariable Long id);

    /***
     * 新增TypeTemplate数据
     * @param typeTemplate
     * @return
     */
    @PostMapping
    Result add(@RequestBody TypeTemplate typeTemplate);

    /***
     * 根据ID查询TypeTemplate数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<TypeTemplate> findById(@PathVariable Long id);

    /***
     * 查询TypeTemplate全部数据
     * @return
     */
    @GetMapping
    Result<List<TypeTemplate>> findAll();
}