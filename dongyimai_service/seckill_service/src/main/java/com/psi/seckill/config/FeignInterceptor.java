package com.psi.seckill.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 拦截器
 * 每次访问微服务都先检查头文件有没有令牌
 * 再把令牌放到下一个请求中
 */
@Configuration
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            //获取Request相关属性
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (requestAttributes != null) {
                //取出request
                HttpServletRequest request = requestAttributes.getRequest();

                //获取所有请求头参数名
                Enumeration<String> headerNames = request.getHeaderNames();

                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();//头文件名
                        String value = request.getHeader(name);//对应的头文件值
                        //把令牌数据添加到请求头中
                        requestTemplate.header(name, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
