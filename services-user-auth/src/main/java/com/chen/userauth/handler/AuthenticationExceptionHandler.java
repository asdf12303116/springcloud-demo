package com.chen.userauth.handler;

import cn.hutool.json.JSONUtil;
import com.chen.common.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chen
 */
@Slf4j
@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        if (authException instanceof InsufficientAuthenticationException) {
            log.debug("登录状态: 未登录，请求地址为: {} ,请求方法为: {}", request.getRequestURI(), request.getMethod());
        } else {
            log.debug("异常类型: " + authException.getClass().getName());
            log.debug(authException.getLocalizedMessage());
        }
        BaseResponse<String> baseResponse = new BaseResponse<>(HttpStatus.UNAUTHORIZED.value(), "用户名或密码错误", "");
        response.getWriter().write(JSONUtil.toJsonStr(baseResponse));
    }
}

