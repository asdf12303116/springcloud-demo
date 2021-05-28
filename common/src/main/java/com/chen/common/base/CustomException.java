package com.chen.common.base;

import lombok.*;

/**
 * @author chen
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomException extends RuntimeException {
    private Integer errorCode;
    private String errorMsg;
}
