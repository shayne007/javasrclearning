package com.feng.springweb.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

@RestControllerAdvice
public class NotAllowExceptionHandler {
    @ExceptionHandler(NotAllowException.class)
    @ResponseBody
    public String handle(NotAllowException e) {
        System.out.println(e.getLocalizedMessage() + " :403");
        return "{\"resultCode\": 403}";
    }
}