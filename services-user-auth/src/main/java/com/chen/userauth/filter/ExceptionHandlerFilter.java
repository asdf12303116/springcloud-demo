package com.chen.userauth.filter;

import cn.hutool.json.JSONUtil;
import com.chen.common.base.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getLocalizedMessage());
            BaseResponse<Map<String, Object>> returnEntity = new BaseResponse(HttpStatus.UNAUTHORIZED.value(), "异常",
                    map);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader("content-type", "application/json; charset=utf-8");
            response.getWriter().write(JSONUtil.toJsonStr(returnEntity));
        }
    }


}
