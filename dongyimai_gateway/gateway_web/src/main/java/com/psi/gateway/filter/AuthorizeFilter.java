package com.psi.gateway.filter;

import com.psi.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/***
 * 全局过滤器
 * 判断请求有没有访问目标资源的权限
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    //令牌头名称
    private final String AUTHORIZE_TOKEN = "authorization";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求体，响应体对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //获取请求的uri  /api/user
        String path = request.getURI().getPath();

        //无需登录即可访问的资源
        if (path.startsWith("/api/user/sendCode")
                || path.startsWith("/api/user/add")
                || path.startsWith("/api/user/login")
//                || path.startsWith("/api/user/load/*")
                || path.startsWith("/api/brand/search/")) {
            Mono<Void> mono = chain.filter(exchange);
            return mono;
        }

        //必须登录才能访问的资源

        //从请求头中获取jwt令牌
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        //如果头文件中没有，则从请求参数里获取
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }

        //从Cookie里获取令牌
        HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
        if (cookie != null) {
            token = cookie.getValue();
        }

        //如果都没有jwt信息，则输出错误代码
        if (StringUtils.isEmpty(token)) {
            //设置方法不允许被访问，错误代码405
            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            Mono<Void> mono = response.setComplete();
            return mono;
        }


        /**
         * 解析jwt令牌信息
         * 如果解析没有发生异常，令牌正确,
         * 如果解析异常，请求携带的令牌错误
         * 把正确的令牌放到请求头中，传递给下一个请求
         */
        try {
/*            Claims claims = JwtUtil.parseJWT(token);
            //将令牌数据添加到头文件
            request.mutate().header(AUTHORIZE_TOKEN, claims.toString());*/

            //不解析里令牌，直接封装到请求头中
            if (token.startsWith("Bearer ") || token.startsWith("bearer ")) {
                request.mutate().header(AUTHORIZE_TOKEN, token);
            } else {
                request.mutate().header(AUTHORIZE_TOKEN, "Bearer " + token);
            }

            return chain.filter(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    /***
     * 过滤器执行顺序，多个过滤器，从小到大执行
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
