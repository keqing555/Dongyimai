package com.psi.user.feign;

import com.psi.entity.Result;
import com.psi.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dym-user")
@RequestMapping("/user")
public interface UserFeign {
    /**
     * 根据username查询用户
     *
     * @param username
     * @return
     */
    @GetMapping("load/{username}")
    Result<User> findByUsername(@PathVariable("username") String username);

    /***
     * 添加用户积分
     * @param points
     * @return
     */
    @GetMapping("add/points")
    Result addUserPoints(@RequestParam("points")  Integer points);
}
