package com.feng.reflect.proxy;

import java.lang.reflect.Proxy;

/**
 * @author fengsy
 * @date 1/19/21
 * @Description
 */

public class JdkProxyFactory {
    public static void main(String[] args) {
        SmsService smsService = (SmsService)JdkProxyFactory.getProxy(new SmsServiceImpl());
        smsService.send("java");
    }

    public static Object getProxy(Object target) {
        // 目标类的类加载器
        // 代理需要实现的接口，可指定多个
        // 代理对象对应的自定义 InvocationHandler
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
            new DebugInvocationHandler(target));
    }
}