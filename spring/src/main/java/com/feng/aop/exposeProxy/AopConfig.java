package com.feng.aop.exposeProxy;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Service;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */

@Aspect
@Service
@Slf4j
public class AopConfig {

	@Around("execution(* com.feng.aop.exposeProxy.ElectricService.pay()) ")
	public void recordPayPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		joinPoint.proceed();
		long end = System.currentTimeMillis();
		System.out.println("Pay method time cost（ms）: " + (end - start));
	}
}