package com.psi.alipay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
@EnableResourceServer
//激活方法上的PreAuthorization注解
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //公钥路径
    private static final String PUBLIC_KEY = "public.key";

    /***
     * 定义jwt令牌转换器
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(this.getPublicKey());
        return converter;
    }

    /***
     * 定义jwt令牌
     * @param jwtAccessTokenConverter
     * @return
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /***
     * 获取非对称加密公钥
     * @return
     */
    private String getPublicKey() {
        try {
            Resource resource = new ClassPathResource(PUBLIC_KEY);
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //设置服务id
        resources.resourceId("dongyimai-pay");
    }

    /***
     * Http安全控制，对每个到达系统的http请求进行安全校验
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //所有的请求必须认证通过
        http.authorizeRequests()
                .antMatchers(//配置放行地址
                        "/spec/**")
                .permitAll().anyRequest().authenticated();
    }
}
