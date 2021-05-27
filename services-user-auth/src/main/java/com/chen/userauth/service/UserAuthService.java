package com.chen.userauth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.userauth.entity.AuthUser;
import com.chen.userauth.entity.dto.UserDto;

import java.util.List;

/**
 * @author chen
 */
public interface UserAuthService extends IService<AuthUser> {
    AuthUser getUserByUsername(String username);
    List<AuthUser> getAllUser();
    Boolean createUser(UserDto userDto);
}
