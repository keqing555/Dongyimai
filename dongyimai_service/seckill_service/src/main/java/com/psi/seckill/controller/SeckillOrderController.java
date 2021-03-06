package com.psi.seckill.controller;

import com.psi.alipay.feign.AlipayFeign;
import com.psi.entity.PageResult;
import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.seckill.bean.SeckillStatus;
import com.psi.seckill.pojo.SeckillOrder;
import com.psi.seckill.service.SeckillOrderService;
import com.psi.utils.TokenDecode;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@Api(tags = "SeckillOrderController")
@RestController
@RequestMapping("/seckillOrder")
@CrossOrigin
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AlipayFeign alipayFeign;

    //订单号
    private static String out_trade_no = "";

    /***
     * SeckillOrder分页条件搜索实现
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "SeckillOrder条件分页查询", notes = "分页条件查询SeckillOrder方法详情", tags = {"SeckillOrderController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true)
    })
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageResult<SeckillOrder>> findPage(@RequestBody(required = false) @ApiParam(name = "SeckillOrder对象", value = "传入JSON数据", required = false) SeckillOrder seckillOrder, @PathVariable int page, @PathVariable int size) {
        //调用SeckillOrderService实现分页条件查询SeckillOrder
        PageResult<SeckillOrder> pageResult = seckillOrderService.findPage(seckillOrder, page, size);
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    /***
     * SeckillOrder分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @ApiOperation(value = "SeckillOrder分页查询", notes = "分页查询SeckillOrder方法详情", tags = {"SeckillOrderController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true)
    })
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageResult<SeckillOrder>> findPage(@PathVariable int page, @PathVariable int size) {
        //调用SeckillOrderService实现分页查询SeckillOrder
        PageResult<SeckillOrder> pageResult = seckillOrderService.findPage(page, size);
        return new Result<PageResult<SeckillOrder>>(true, StatusCode.OK, "查询成功", pageResult);
    }

    /***
     * 多条件搜索品牌数据
     * @param seckillOrder
     * @return
     */
    @ApiOperation(value = "SeckillOrder条件查询", notes = "条件查询SeckillOrder方法详情", tags = {"SeckillOrderController"})
    @PostMapping(value = "/search")
    public Result<List<SeckillOrder>> findList(@RequestBody(required = false) @ApiParam(name = "SeckillOrder对象", value = "传入JSON数据", required = false) SeckillOrder seckillOrder) {
        //调用SeckillOrderService实现条件查询SeckillOrder
        List<SeckillOrder> list = seckillOrderService.findList(seckillOrder);
        return new Result<List<SeckillOrder>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation(value = "SeckillOrder根据ID删除", notes = "根据ID删除SeckillOrder方法详情", tags = {"SeckillOrderController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        //调用SeckillOrderService实现根据主键删除
        seckillOrderService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     * @param id
     * @return
     */
    @ApiOperation(value = "SeckillOrder根据ID修改", notes = "根据ID修改SeckillOrder方法详情", tags = {"SeckillOrderController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody @ApiParam(name = "SeckillOrder对象", value = "传入JSON数据", required = false) SeckillOrder seckillOrder, @PathVariable Long id) {
        //设置主键值
        seckillOrder.setId(id);
        //调用SeckillOrderService实现修改SeckillOrder
        seckillOrderService.update(seckillOrder);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增SeckillOrder数据
     * @param seckillOrder
     * @return
     */
    @ApiOperation(value = "SeckillOrder添加", notes = "添加SeckillOrder方法详情", tags = {"SeckillOrderController"})
    @PostMapping
    public Result add(@RequestBody @ApiParam(name = "SeckillOrder对象", value = "传入JSON数据", required = true) SeckillOrder seckillOrder) {
        //调用SeckillOrderService实现添加SeckillOrder
        seckillOrderService.add(seckillOrder);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询SeckillOrder数据
     * @param id
     * @return
     */
    @ApiOperation(value = "SeckillOrder根据ID查询", notes = "根据ID查询SeckillOrder方法详情", tags = {"SeckillOrderController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public Result<SeckillOrder> findById(@PathVariable Long id) {
        //调用SeckillOrderService实现根据主键查询SeckillOrder
        SeckillOrder seckillOrder = seckillOrderService.findById(id);
        return new Result<SeckillOrder>(true, StatusCode.OK, "查询成功", seckillOrder);
    }

    /***
     * 查询SeckillOrder全部数据
     * @return
     */
    @ApiOperation(value = "查询所有SeckillOrder", notes = "查询所SeckillOrder有方法详情", tags = {"SeckillOrderController"})
    @GetMapping
    public Result<List<SeckillOrder>> findAll() {
        //调用SeckillOrderService实现查询所有SeckillOrder
        List<SeckillOrder> list = seckillOrderService.findAll();
        return new Result<List<SeckillOrder>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 添加秒杀订单到redis
     * @param time
     * @param id
     * @return
     */
    @GetMapping("addSeckillOrder")
    public Result addSeckillOrder(String time, long id) {
        //从令牌里获取用户名
        String username = tokenDecode.getUserInfo().get("user_name");

        //添加订单
        String message = seckillOrderService.addSeckillOrder(id, time, username);

        //调用支付服务，创建秒杀二维码
        Map<String, String> map = alipayFeign.createNative();

        //获取订单号
        out_trade_no = map.get("out_trade_no");

        System.out.println("生成的抢购订单号：" + out_trade_no);

        return new Result(true, StatusCode.OK, message, map);
    }


    @PostMapping("afterPaySeckill")
    public Result afterPaySeckill(@RequestBody Map seckillMap, @RequestParam("trade_no") String trade_no) {
        Map<String, String> map = alipayFeign.createNative();
        return new Result(true, StatusCode.OK, "创建秒杀支付二维码", map);
    }

    /***
     * 查询抢购状态
     * @return
     */
    @GetMapping("queryStatus")
    public Result queryStatus() {
        if ("".equals(out_trade_no)) {
            return new Result(false, StatusCode.ERROR, "没有抢购订单");
        }
        try {
            String username = tokenDecode.getUserInfo().get("user_name");

            SeckillStatus seckillStatus = seckillOrderService.queryStatus(username);

            System.out.println("要查询抢购订单号：" + out_trade_no);

            int count = 0;
            while (true) {
                try {
                    //返回抢购支付状态
                    Thread.sleep(1000);
                    Result payStatus = alipayFeign.getPayStatus(out_trade_no);
                    payStatus.setData(seckillStatus);
                    if (payStatus.isFlag()) {
                        //交易完成，订单，支付日志保存到MySQL，删除redis订单

                    }
                    System.out.println("抢购支付成功");
                    return payStatus;
                } catch (Exception e) {
                    System.out.println("未获取到支付状态");
                }
                //超过两分钟
                count++;
                if (count > 120) {
                    break;
                }
            }
            return new Result(false, StatusCode.ERROR, "查询抢单状态失败", seckillStatus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "查询抢购状态失败");
        }

    }
}
