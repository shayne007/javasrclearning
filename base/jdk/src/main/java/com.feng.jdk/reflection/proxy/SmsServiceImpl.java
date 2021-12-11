package com.feng.jdk.reflection.proxy;

/**
 * @author fengsy
 * @date 1/19/21
 * @Description
 */
public final class SmsServiceImpl implements SmsService {
    @Override
    public String send(String message) {
        System.out.println("send message:" + message);
        return message;
    }
}