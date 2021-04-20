package com.feng.javasrc.reflect.proxy;

/**
 * @author fengsy
 * @date 1/19/21
 * @Description
 */
public class SmsServiceImpl implements SmsService {
    @Override
    public String send(String message) {
        System.out.println("send message:" + message);
        return message;
    }
}