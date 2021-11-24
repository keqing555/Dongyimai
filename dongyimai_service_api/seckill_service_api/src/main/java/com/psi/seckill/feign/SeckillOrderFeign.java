package com.psi.seckill.feign;
import com.psi.entity.PageResult;
import com.psi.entity.Result;
import com.psi.seckill.pojo.SeckillOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@FeignClient(value="dym-seckill")
@RequestMapping("/seckillOrder")
public interface SeckillOrderFeign {

    /***
     * SeckillOrder分页条件搜索实现
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}" )
    Result<PageResult<SeckillOrder>> findPage(@RequestBody(required = false) SeckillOrder seckillOrder, @PathVariable  int page, @PathVariable  int size);

    /***
     * SeckillOrder分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}" )
    Result<PageResult<SeckillOrder>> findPage(@PathVariable  int page, @PathVariable  int size);

    /***
     * 多条件搜索品牌数据
     * @param seckillOrder
     * @return
     */
    @PostMapping("/search" )
    Result<List<SeckillOrder>> findList(@RequestBody(required = false) SeckillOrder seckillOrder);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}" )
    Result delete(@PathVariable Long id);

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    Result update(@RequestBody SeckillOrder seckillOrder,@PathVariable Long id);

    /***
     * 新增SeckillOrder数据
     * @param seckillOrder
     * @return
     */
    @PostMapping
    Result add(@RequestBody SeckillOrder seckillOrder);

    /***
     * 根据ID查询SeckillOrder数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<SeckillOrder> findById(@PathVariable Long id);

    /***
     * 查询SeckillOrder全部数据
     * @return
     */
    @GetMapping
    Result<List<SeckillOrder>> findAll();
}