package com.chen.userauth.controller;

import com.chen.common.base.BaseController;
import com.chen.common.base.BaseResponse;
import com.chen.userauth.entity.dto.LoginUserDto;
import com.chen.userauth.service.JwtService;
import com.nimbusds.jose.JOSEException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author chen
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {
    @Resource
    private JwtService jwtService;


    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Map<String, String>>> login(@RequestBody LoginUserDto loginUserDto) throws JOSEException {
        String token = jwtService.login(loginUserDto);

        return buildResponse(HttpStatus.OK, 100000, "登陆成功", Map.of("token", token));
    }

}
