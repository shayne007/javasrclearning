package com.feng.aop.proxyAttributeUnreachable;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */
public class User {
    private String payNum;

    public User(String payNum) {
        this.payNum = payNum;
    }

    public String getPayNum() {
        return payNum;
    }

}