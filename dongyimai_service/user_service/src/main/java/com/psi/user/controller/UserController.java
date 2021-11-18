package com.psi.user.controller;

import com.alibaba.fastjson.JSON;
import com.psi.entity.PageResult;
import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.user.pojo.User;
import com.psi.user.service.UserService;
import com.psi.utils.BCrypt;
import com.psi.utils.JwtUtil;
import com.psi.utils.PhoneFormatCheckUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@Api(tags = "UserController")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    /***
     * User分页条件搜索实现
     * @param user
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "User条件分页查询", notes = "分页条件查询User方法详情", tags = {"UserController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true)
    })
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageResult<User>> findPage(@RequestBody(required = false) @ApiParam(name = "User对象", value = "传入JSON数据", required = false) User user, @PathVariable int page, @PathVariable int size) {
        //调用UserService实现分页条件查询User
        PageResult<User> pageResult = userService.findPage(user, page, size);
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    /***
     * User分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @ApiOperation(value = "User分页查询", notes = "分页查询User方法详情", tags = {"UserController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true)
    })
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageResult<User>> findPage(@PathVariable int page, @PathVariable int size) {
        //调用UserService实现分页查询User
        PageResult<User> pageResult = userService.findPage(page, size);
        return new Result<PageResult<User>>(true, StatusCode.OK, "查询成功", pageResult);
    }

    /***
     * 多条件搜索品牌数据
     * @param user
     * @return
     */
    @ApiOperation(value = "User条件查询", notes = "条件查询User方法详情", tags = {"UserController"})
    @PostMapping(value = "/search")
    public Result<List<User>> findList(@RequestBody(required = false) @ApiParam(name = "User对象", value = "传入JSON数据", required = false) User user) {
        //调用UserService实现条件查询User
        List<User> list = userService.findList(user);
        return new Result<List<User>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation(value = "User根据ID删除", notes = "根据ID删除User方法详情", tags = {"UserController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        //调用UserService实现根据主键删除
        userService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改User数据
     * @param user
     * @param id
     * @return
     */
    @ApiOperation(value = "User根据ID修改", notes = "根据ID修改User方法详情", tags = {"UserController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody @ApiParam(name = "User对象", value = "传入JSON数据", required = false) User user, @PathVariable Long id) {
        //设置主键值
        user.setId(id);
        //调用UserService实现修改User
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增User数据
     * @param user
     * @return
     */
    @ApiOperation(value = "User添加", notes = "添加User方法详情", tags = {"UserController"})
    @PostMapping
    public Result add(@RequestBody @ApiParam(name = "User对象", value = "传入JSON数据", required = true) User user) {
        //调用UserService实现添加User
        userService.add(user);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询User数据
     * @param id
     * @return
     */
    //表示admin,root权限才能访问该方法（也可加在类上）
    @PreAuthorize("hasAnyAuthority('admin','root')")
    @ApiOperation(value = "User根据ID查询", notes = "根据ID查询User方法详情", tags = {"UserController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable Long id) {
        //调用UserService实现根据主键查询User
        User user = userService.findById(id);
        return new Result<User>(true, StatusCode.OK, "查询成功", user);
    }

    /***
     * 查询User全部数据
     * @return
     */
    @ApiOperation(value = "查询所有User", notes = "查询所User有方法详情", tags = {"UserController"})
    @GetMapping
    public Result<List<User>> findAll(HttpServletRequest request) {
        //获取令牌信息
        String authorization = request.getHeader("Authorization");
        System.out.println("令牌信息：" + authorization);
        //调用UserService实现查询所有User
        List<User> list = userService.findAll();
        return new Result<List<User>>(true, StatusCode.OK, "查询成功", list);
    }


    /***
     * 发送验证码
     * @param phone
     * @return
     */
    @GetMapping("/sendCode")
    public Result sendCode(String phone) {
        //判断手机号格式
        if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
            return new Result(false, StatusCode.ERROR, "手机号格式不正确");
        }

        try {
            userService.createSmsCode(phone);
            return new Result(true, StatusCode.OK, "验证码已发送，请注意查收");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "验证码发送失败，请重试");
        }

    }

    /***
     * 注册用户
     * @param user
     * @param code
     * @return
     */
    @PostMapping("add")
    public Result add(@RequestBody User user, String code) {
        boolean bool = userService.checkSmsCode(user.getPhone(), code);
        if (bool == false) {
            return new Result(false, StatusCode.ERROR, "验证码错误");
        }
        try {
            userService.add(user);
            return new Result(true, StatusCode.OK, "注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "注册失败");
        }
    }

    /***
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public Result<String> login(String username, String password, HttpServletResponse response) {
        //根据用户名获取用户信息
        User user = userService.findByUsername(username);

        if (user != null) {
            if (BCrypt.checkpw(password, user.getPassword())) {
                //登录成功签发jwt-token令牌
                Map<String, Object> map = new HashMap<>();
                map.put("role", "USER");
                map.put("success", "SUCCESS");
                map.put("username", username);
                //生成令牌
                String jwt = JwtUtil.createJWT(UUID.randomUUID().toString(),
                        JSON.toJSONString(map),
                        null);
                //添加到cookie
                Cookie cookie = new Cookie("Authorization", jwt);
                cookie.setPath("/");
                cookie.setMaxAge(2 * 60 * 1000);//cookie有效时间
                response.addCookie(cookie);
                //返回jwt
                return new Result<>(true, StatusCode.OK, "登录成功", jwt);
            }
            return new Result<>(false, StatusCode.ERROR, "密码错误");
        }
        return new Result<>(false, StatusCode.ERROR, "没有该用户");
    }

    @GetMapping("load/{username}")
    public Result findByUsername(@PathVariable("usernmae") String username) {
        User user = userService.findByUsername(username);
        return new Result(true, StatusCode.OK, "查询成功", user);
    }
}
