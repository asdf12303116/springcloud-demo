package com.chen.userauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.userauth.entity.AuthUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chen
 */
@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUser> {
}
