package com.psi.sellergoods.feign;
import com.psi.entity.PageResult;
import com.psi.entity.Result;
import com.psi.sellergoods.pojo.Specification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@FeignClient(value="sellergoods")
@RequestMapping("/specification")
public interface SpecificationFeign {

    /***
     * Specification分页条件搜索实现
     * @param specification
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}" )
    Result<PageResult<Specification>> findPage(@RequestBody(required = false) Specification specification, @PathVariable  int page, @PathVariable  int size);

    /***
     * Specification分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}" )
    Result<PageResult<Specification>> findPage(@PathVariable  int page, @PathVariable  int size);

    /***
     * 多条件搜索品牌数据
     * @param specification
     * @return
     */
    @PostMapping("/search" )
    Result<List<Specification>> findList(@RequestBody(required = false) Specification specification);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}" )
    Result delete(@PathVariable Long id);

    /***
     * 修改Specification数据
     * @param specification
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    Result update(@RequestBody Specification specification,@PathVariable Long id);

    /***
     * 新增Specification数据
     * @param specification
     * @return
     */
    @PostMapping
    Result add(@RequestBody Specification specification);

    /***
     * 根据ID查询Specification数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Specification> findById(@PathVariable Long id);

    /***
     * 查询Specification全部数据
     * @return
     */
    @GetMapping
    Result<List<Specification>> findAll();
}