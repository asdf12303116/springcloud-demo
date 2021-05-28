package com.chen.userauth.controller;

import com.chen.userauth.entity.AuthUser;
import com.chen.userauth.entity.dto.UserDto;
import com.chen.userauth.service.JwtService;
import com.chen.userauth.service.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

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
    public List<AuthUser> getAllUser() {
        return userAuthService.getAllUser();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<AuthUser> getUser(@PathVariable("username") String username) {
        Optional<AuthUser> authUser = Optional.ofNullable(userAuthService.getUserByUsername(username));
        return ResponseEntity.of(authUser);
    }

    @PostMapping("/user")
    public void createUser(@RequestBody UserDto user) {
        userAuthService.createUser(user);
    }

}
