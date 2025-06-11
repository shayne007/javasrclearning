package com.feng.aop.proxyAttributeUnreachable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */

@Aspect
@Service
@Slf4j
public class AdminAopConfig {
    @Before("execution(* com.feng.aop.proxyAttributeUnreachable.AdminUserService.login(..)) ")
    public void logAdminLogin(JoinPoint joinPoint) throws Throwable {
        System.out.println("! admin login");
    }
}