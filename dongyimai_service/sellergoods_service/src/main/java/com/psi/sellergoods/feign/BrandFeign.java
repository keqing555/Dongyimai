package com.psi.sellergoods.feign;
import com.psi.entity.PageResult;
import com.psi.entity.Result;
import com.psi.sellergoods.pojo.Brand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@FeignClient(value="sellergoods")
@RequestMapping("/brand")
public interface BrandFeign {

    /***
     * Brand分页条件搜索实现
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}" )
    Result<PageResult<Brand>> findPage(@RequestBody(required = false) Brand brand, @PathVariable  int page, @PathVariable  int size);

    /***
     * Brand分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}" )
    Result<PageResult<Brand>> findPage(@PathVariable  int page, @PathVariable  int size);

    /***
     * 多条件搜索品牌数据
     * @param brand
     * @return
     */
    @PostMapping("/search" )
    Result<List<Brand>> findList(@RequestBody(required = false) Brand brand);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}" )
    Result delete(@PathVariable Long id);

    /***
     * 修改Brand数据
     * @param brand
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    Result update(@RequestBody Brand brand,@PathVariable Long id);

    /***
     * 新增Brand数据
     * @param brand
     * @return
     */
    @PostMapping
    Result add(@RequestBody Brand brand);

    /***
     * 根据ID查询Brand数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Brand> findById(@PathVariable Long id);

    /***
     * 查询Brand全部数据
     * @return
     */
    @GetMapping
    Result<List<Brand>> findAll();
}