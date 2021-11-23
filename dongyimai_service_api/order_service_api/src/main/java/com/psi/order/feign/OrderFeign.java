package com.psi.order.feign;

import com.psi.entity.PageResult;
import com.psi.entity.Result;
import com.psi.order.pojo.Order;
import com.psi.order.pojo.PayLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@FeignClient(value = "dym-order")
@RequestMapping("/order")
public interface OrderFeign {

    /***
     * Order分页条件搜索实现
     * @param order
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    Result<PageResult<Order>> findPage(@RequestBody(required = false) Order order, @PathVariable int page, @PathVariable int size);

    /***
     * Order分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    Result<PageResult<Order>> findPage(@PathVariable int page, @PathVariable int size);

    /***
     * 多条件搜索品牌数据
     * @param order
     * @return
     */
    @PostMapping("/search")
    Result<List<Order>> findList(@RequestBody(required = false) Order order);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    Result delete(@PathVariable Long id);

    /***
     * 修改Order数据
     * @param order
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    Result update(@RequestBody Order order, @PathVariable Long id);

    /***
     * 新增Order数据
     * @param order
     * @return
     */
    @PostMapping
    Result add(@RequestBody Order order);

    /***
     * 根据ID查询Order数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Order> findById(@PathVariable Long id);

    /***
     * 查询Order全部数据
     * @return
     */
    @GetMapping
    Result<List<Order>> findAll();

    /***
     * 从redis里查询支付日志
     * @param username
     * @return
     */
    @GetMapping("payLog/{username}")
    Result<PayLog> getPayLogFromRedis(@PathVariable(name = "username") String username);

    /***
     * 支付完成后，修改订单状态
     * @param out_trade_no
     * @param transactionId
     * @return
     */
    @PostMapping("updateOrderStatus")
    public Result updateOrderStatus(
            @RequestParam(name = "out_trade_no") String out_trade_no,
            @RequestParam(value = "transactionId") String transactionId);

}