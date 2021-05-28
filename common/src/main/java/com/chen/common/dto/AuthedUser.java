package com.chen.common.dto;

import com.chen.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author chen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthedUser extends BaseEntity {
    private String username;
    private String userType;
    private Set<String> authorities;
}
