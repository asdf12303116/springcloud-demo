package com.chen.userauth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.chen.common.base.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author chen
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public  class BaseAuthUser extends BaseEntity {
    @TableField("user_name")
    private String username;
    private String nickName;
    private String password;
    private String userType;
    private Boolean expireStatus;
    private Boolean lockStatus;
    private Boolean enable;

    @Builder
    BaseAuthUser(String username,String nickName,String password,String userType){
        this.username=username;
        this.nickName=nickName;
        this.password=password;
        this.userType=userType;
    }
}
