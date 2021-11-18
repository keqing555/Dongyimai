package com.psi.oauth.service.impl;

import com.psi.oauth.service.AuthService;
import com.psi.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret) {

        //申请令牌
        AuthToken authToken = this.applyToken(username, password, clientId, clientSecret);

        if (authToken == null) {
            throw new RuntimeException("申请令牌失败");
        }

        return authToken;
    }

    /**
     * 认证方法
     *
     * @param username     用户登录名
     * @param password     用户密码
     * @param clientId     客户端id
     * @param clientSecret 客户端密码
     * @return
     */
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {
        //选中认证服务的地址
        ServiceInstance instance = loadBalancerClient.choose("user-auth");

        if (instance == null) {
            throw new RuntimeException("未找到 USER-AUTH 服务");
        }

        //获取令牌url
        String path = instance.getUri().toString() + "/oauth/token";

        //定义body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");//密码模式
        body.add("username", username);//账号名
        body.add("password", password);//密码

        //定义头
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", this.httpBasic(clientId, clientSecret));

        //如果想捕捉服务本身抛出的异常信息，需要通过自行实现RestTemplate的ErrorHandler。
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时，也要正常响应，不要抛出异常
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });


        Map map = null;

        try {
            //http请求Spring Security 的申请令牌接口
            ResponseEntity<Map> exchange = restTemplate.exchange(
                    path,
                    HttpMethod.POST,
                    new HttpEntity<MultiValueMap<String, String>>(body, headers),
                    Map.class);
            //获取响应数据
            map = exchange.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

        if (map == null || StringUtils.isEmpty(map.get("access_token"))) {
            throw new RuntimeException("创建令牌失败");
        }

        //把响应数据分装成AuthToken对象
        AuthToken authToken = new AuthToken();
        String accessToken = (String) map.get("access_token");
        String refresh_token = (String) map.get("refresh_token");
        String jti = map.get("jti") + "";

        authToken.setAccessToken(accessToken);
        authToken.setRefreshToken(refresh_token);
        authToken.setJti(jti);

        return authToken;
    }


    private String httpBasic(String clientId, String clientSecret) {
        //拼接 clientId:clientSecret
        String str = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64Utils.encode(str.getBytes());
        //返回授权信息字符串
        return "Basic " + new String(encode);
    }

}
