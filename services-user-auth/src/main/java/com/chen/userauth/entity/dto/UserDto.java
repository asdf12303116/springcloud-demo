package com.chen.userauth.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author chen
 */
@Data
@EqualsAndHashCode
public class UserDto {
    @NotNull
    private String username;
    @NotNull
    private String nickName;
    @NotNull
    private String password;
    @NotNull
    private String userType;
}
