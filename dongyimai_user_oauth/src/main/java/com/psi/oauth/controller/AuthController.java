package com.psi.oauth.controller;

import com.psi.oauth.service.AuthService;
import com.psi.oauth.util.AuthToken;
import com.psi.oauth.util.CookieUtil;
import com.psi.oauth.util.Result;
import com.psi.oauth.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class AuthController {

    @Value("${auth.clientId}")
    private String clientId;//客户端id

    @Value("${auth.clientSecret}")
    private String clientSecret;//客户端密码

    @Value("${auth.cookieDomain}")
    private String cookieDomain;//cookie储存的域名

    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;//cookie生命周期

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public Result login(String username, String password, HttpServletResponse response) {
        if (StringUtils.isEmpty(username)) {
            return new Result(false, StatusCode.ERROR, "用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            return new Result(false, StatusCode.ERROR, "密码不能为空");
        }
        //申请令牌
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);

        //获取用户身份令牌
        String accessToken = authToken.getAccessToken();

        //把令牌存储到cookie
        CookieUtil.addCookie(
                response,
                cookieDomain,            //cookie域名
                "/",                //
                "Authorization",   //cookie名字
                accessToken,            //cookie值
                cookieMaxAge,           //cookie生命周期
                false);
//
//        Cookie cookie = new Cookie("Authorization", accessToken);
//        response.addCookie(cookie);
//
        return new Result(true, StatusCode.OK, "登录成功");
    }
}
