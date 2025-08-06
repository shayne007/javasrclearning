package com.feng.springcore.lifecycle.extension;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 扩展点：BeanPostProcessor
 *
 * @since 2025/8/6
 */
public class HelloBeanPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws
			BeansException {
		if ("hello".equals(beanName)) {
			System.out.println("Hello postProcessBeforeInitialization");
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (beanName.contains("hello")) {
			System.out.println("Hello postProcessAfterInitialization");
		}
		return bean;
	}
}
