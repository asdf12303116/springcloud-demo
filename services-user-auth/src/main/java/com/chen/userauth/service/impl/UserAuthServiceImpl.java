package com.chen.userauth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.service.RedisService;
import com.chen.userauth.entity.AuthUser;
import com.chen.userauth.entity.BaseAuthUser;
import com.chen.userauth.entity.dto.UserDto;
import com.chen.userauth.mapper.AuthUserMapper;
import com.chen.userauth.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author chen
 */
@Service
@Slf4j
public class UserAuthServiceImpl extends ServiceImpl<AuthUserMapper, BaseAuthUser> implements UserAuthService,
        UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RedisService redisService;


    @Override
    public BaseAuthUser getUserByUsername(String username) {
        BaseAuthUser authUserT = redisService.get("test");
        if (Objects.nonNull(authUserT)) {
            return authUserT;
        }
        BaseAuthUser authUser = lambdaQuery().eq(BaseAuthUser::getUsername, username).one();
        redisService.put("test", authUser);
        return authUser;

    }

    @Override
    public List<BaseAuthUser> getAllUser() {
        return lambdaQuery().list();
    }

    @Override
    public Boolean createUser(UserDto userDto) {
        try {
            BaseAuthUser baseAuthUser=BaseAuthUser.builder()
                    .username(userDto.getUsername())
                    .nickName(userDto.getNickName())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .userType(userDto.getUserType())
                    .build();
            AuthUser authUser=new AuthUser();
            BeanUtils.copyProperties(baseAuthUser,authUser);
            var tmp = baseMapper.insert(authUser);
            log.debug("{}",tmp);
            return true;
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
            return false;
        }


    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = new AuthUser();
        BaseAuthUser baseAuthUser = getUserByUsername(username);
        BeanUtils.copyProperties(baseAuthUser, authUser);
        return authUser;
    }
}
