package com.feng.aop.priorityDisorder;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */
@Aspect
@Service
@Slf4j
public class AspectServiceOrderByPriority {
    @Before("execution(* com.feng.aop.proxyAttribute.AdminElectricService.charge()) ")
    public void checkAuthority(JoinPoint joinPoint) throws Throwable {
        System.out.println("validating user authority");
        Thread.sleep(1000);
    }

    @Around("execution(* com.feng.aop.proxyAttribute.AdminElectricService.doCharge()) ")
    public void logCostTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        System.out.println("Charge method time cost（ms）: " + (end - start));
    }
}