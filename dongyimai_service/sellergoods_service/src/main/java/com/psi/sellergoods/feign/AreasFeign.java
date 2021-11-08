package com.psi.sellergoods.feign;
import com.psi.entity.PageResult;
import com.psi.entity.Result;
import com.psi.sellergoods.pojo.Areas;
import com.psi.sellergoods.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@FeignClient(value="sellergoods")
@RequestMapping("/areas")
public interface AreasFeign {

    /***
     * Areas分页条件搜索实现
     * @param areas
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}" )
    Result<PageResult<Areas>> findPage(@RequestBody(required = false) Areas areas, @PathVariable  int page, @PathVariable  int size);

    /***
     * Areas分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}" )
    Result<PageResult<Areas>> findPage(@PathVariable  int page, @PathVariable  int size);

    /***
     * 多条件搜索品牌数据
     * @param areas
     * @return
     */
    @PostMapping("/search" )
    Result<List<Areas>> findList(@RequestBody(required = false) Areas areas);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}" )
    Result delete(@PathVariable Integer id);

    /***
     * 修改Areas数据
     * @param areas
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    Result update(@RequestBody Areas areas,@PathVariable Integer id);

    /***
     * 新增Areas数据
     * @param areas
     * @return
     */
    @PostMapping
    Result add(@RequestBody Areas areas);

    /***
     * 根据ID查询Areas数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Areas> findById(@PathVariable Integer id);

    /***
     * 查询Areas全部数据
     * @return
     */
    @GetMapping
    Result<List<Areas>> findAll();
}