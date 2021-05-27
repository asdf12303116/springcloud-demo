package com.chen.common.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chen
 */
@RestController
public class BaseController {

    private <T> ResponseEntity<BaseResponse<T>> buildResponse(HttpStatus httpStatus, Integer customCode, String message, T data){
        BaseResponse<T> baseResponse=new BaseResponse<>(customCode,message,data);
        return new ResponseEntity<>( baseResponse,httpStatus);
    }
}
