package com.feng.springweb.exception;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

public class NotAllowException extends RuntimeException {
    public NotAllowException(String msg) {
        super(msg);
    }
}