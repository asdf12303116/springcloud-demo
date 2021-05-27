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
    private KeyPair keyPair;

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

    @GetMapping("getPublicKey")
    public Map<String, Object> getPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

    private Map<String, String> getHeadersInfo() {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}
