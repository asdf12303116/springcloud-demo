package com.chen.userauth.controller;

import com.chen.userauth.entity.AuthUser;
import com.chen.userauth.entity.dto.UserDto;
import com.chen.userauth.service.UserAuthService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

/**
 * @author chen
 */
@RestController
public class TestController {
    @Resource
    private UserAuthService userAuthService;
    @Resource
    private JwtService jwtService;


    @Resource
    private HttpServletRequest request;

    @GetMapping("/user")
    public List<AuthUser> getAllUser(){
        return userAuthService.getAllUser();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<AuthUser>  getUser(@PathVariable("username") String username){
        Optional<AuthUser> authUser=Optional.ofNullable(userAuthService.getUserByUsername(username));
        return ResponseEntity.of(authUser);
    }

    @PostMapping("/user")
    public void createUser(@RequestBody UserDto user){
        userAuthService.createUser(user);
    }

}
