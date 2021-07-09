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
@Order(2)
public class AspectServiceFirst {
    @Before("execution(* com.feng.aop.proxyAttributeUnreachable.AdminElectricService.charge()) ")
    public void logBeforeMethod(JoinPoint joinPoint) throws Throwable {
        System.out.println("step into ->" + joinPoint.getSignature());
    }
}