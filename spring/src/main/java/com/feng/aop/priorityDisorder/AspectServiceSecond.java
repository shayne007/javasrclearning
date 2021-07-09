package com.feng.aop.priorityDisorder;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
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
@Order(1)
public class AspectServiceSecond {

    @Before("execution(* com.feng.aop.proxyAttributeUnreachable.AdminElectricService.charge()) ")
    public void checkAuthority(JoinPoint joinPoint) throws Throwable {
        // public void validateAuthority(JoinPoint joinPoint) throws Throwable {
        throw new RuntimeException("authority check failed");
    }
}