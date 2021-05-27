package com.chen.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chen
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class BaseResponse<T> {
    private Integer statusCode;
    private String statusMessage;
    private T data;

}
