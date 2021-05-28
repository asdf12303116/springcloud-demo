package com.chen.userauth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.userauth.entity.BaseAuthUser;
import com.chen.userauth.entity.dto.UserDto;

import java.util.List;

/**
 * @author chen
 */
public interface UserAuthService extends IService<BaseAuthUser> {
    BaseAuthUser getUserByUsername(String username);

    List<BaseAuthUser> getAllUser();

    Boolean createUser(UserDto userDto);
}
