package com.chen.gateway.filter;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chen.common.base.BaseResponse;
import com.nimbusds.jose.JWSObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/** JWT信息写入每个请求，业务直接从Header获取用户信息
 * @author chen
 */
@Slf4j
@Component
public class AutoWriteTokenFilter implements GlobalFilter, Ordered {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();


        // 解析JWT获取jti，以jti为key判断redis的黑名单列表是否存在，存在拦截响应token失效
        String token = request.getHeaders().getFirst("Authorization");
        token = token.replace("Bearer ", Strings.EMPTY);
        JWSObject jwsObject = JWSObject.parse(token);
        String payload = jwsObject.getPayload().toString();
        JSONObject jsonObject = JSONUtil.parseObj(payload);
        String jti = jsonObject.getStr("client_id");

        Optional<Boolean> isBlack= Optional.ofNullable(redisTemplate.hasKey("auth::"+"BLACKLIST"+jti));
        if (isBlack.orElse(false)){
            log.info("token无效");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            BaseResponse<Object> res= new BaseResponse<>(43000,"token无效",null);
            DataBuffer buffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(res).getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));

        }



        // 存在token且不是黑名单，request写入JWT的载体信息
        request = exchange.getRequest().mutate()
                .header("userId", jsonObject.getStr("userId"))
                .build();
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
