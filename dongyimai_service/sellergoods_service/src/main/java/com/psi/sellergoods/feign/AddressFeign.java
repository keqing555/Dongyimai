package com.psi.sellergoods.feign;
import com.psi.entity.PageResult;
import com.psi.entity.Result;
import com.psi.sellergoods.pojo.Address;
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
@RequestMapping("/address")
public interface AddressFeign {

    /***
     * Address分页条件搜索实现
     * @param address
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}" )
    Result<PageResult<Address>> findPage(@RequestBody(required = false) Address address, @PathVariable  int page, @PathVariable  int size);

    /***
     * Address分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}" )
    Result<PageResult<Address>> findPage(@PathVariable  int page, @PathVariable  int size);

    /***
     * 多条件搜索品牌数据
     * @param address
     * @return
     */
    @PostMapping("/search" )
    Result<List<Address>> findList(@RequestBody(required = false) Address address);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}" )
    Result delete(@PathVariable Long id);

    /***
     * 修改Address数据
     * @param address
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    Result update(@RequestBody Address address,@PathVariable Long id);

    /***
     * 新增Address数据
     * @param address
     * @return
     */
    @PostMapping
    Result add(@RequestBody Address address);

    /***
     * 根据ID查询Address数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Address> findById(@PathVariable Long id);

    /***
     * 查询Address全部数据
     * @return
     */
    @GetMapping
    Result<List<Address>> findAll();
}