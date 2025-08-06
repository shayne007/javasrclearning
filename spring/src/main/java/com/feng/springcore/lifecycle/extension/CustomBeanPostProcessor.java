package com.feng.springcore.lifecycle.extension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @since 2025/8/6
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {

	private static final Logger logger =
			LoggerFactory.getLogger(CustomBeanPostProcessor.class);

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {

		logger.info("12. BeanPostProcessor.postProcessBeforeInitialization for bean: {}",
				beanName);

		// Custom logic before initialization
		if (bean instanceof InitializationAware) {
			((InitializationAware) bean).beforeInitialization();
		}

		// Inject custom dependencies or modify bean state
		injectCustomDependencies(bean, beanName);

		return bean; // Return the bean (could return a proxy)
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {

		logger.info("16. BeanPostProcessor.postProcessAfterInitialization for bean: {}",
				beanName);

		// Custom logic after initialization
		if (bean instanceof InitializationAware) {
			((InitializationAware) bean).afterInitialization();
		}

		// Could wrap bean in proxy for AOP-like functionality
		return wrapIfNecessary(bean, beanName);
	}

	private void injectCustomDependencies(Object bean, String beanName) {
		// Example: Inject configuration based on bean name or type
		if (bean instanceof ConfigurationAware) {
			CustomConfiguration config = loadConfigurationFor(beanName);
			((ConfigurationAware) bean).setConfiguration(config);
		}
	}

	private Object wrapIfNecessary(Object bean, String beanName) {
		// Example: Create proxy for beans with specific annotations
		if (bean.getClass().isAnnotationPresent(Monitored.class)) {
			return createMonitoringProxy(bean, beanName);
		}
		return bean;
	}

	private CustomConfiguration loadConfigurationFor(String beanName) {
		// Load configuration logic
		return new CustomConfiguration();
	}

	private Object createMonitoringProxy(Object bean, String beanName) {
		// Create monitoring proxy
		return Proxy.newProxyInstance(
				bean.getClass().getClassLoader(),
				bean.getClass().getInterfaces(),
				(proxy, method, args) -> {
					long start = System.currentTimeMillis();
					try {
						Object result = method.invoke(bean, args);
						long end = System.currentTimeMillis();
						logger.info("Method {} on bean {} took {} ms",
								method.getName(), beanName, (end - start));
						return result;
					}
					catch (InvocationTargetException e) {
						throw e.getTargetException();
					}
				}
		);
	}
}


