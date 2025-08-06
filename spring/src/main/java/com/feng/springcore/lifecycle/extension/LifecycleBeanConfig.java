package com.feng.springcore.lifecycle.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * TODO
 *
 * @since 2025/8/6
 */
@Configuration
public class LifecycleBeanConfig {

	private static final Logger logger =
			LoggerFactory.getLogger(CustomBeanPostProcessor.class);

	@Bean(initMethod = "customInitMethod", destroyMethod = "customDestroyMethod")
	public ComprehensiveLifecycleBean lifecycleBean() {
		return new ComprehensiveLifecycleBean();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public PrototypeBean prototypeBean() {
		return new PrototypeBean();
	}

	// Lifecycle for prototype beans
	@Bean
	public BeanDefinitionRegistryPostProcessor prototypeBeanDestroyerPostProcessor() {
		return new BeanDefinitionRegistryPostProcessor() {

			@Override
			public void postProcessBeanFactory(
					ConfigurableListableBeanFactory beanFactory)
					throws BeansException {
				// No implementation needed
			}

			@Override
			public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
					throws BeansException {

				// Register shutdown hook for prototype beans
				Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					// Cleanup prototype beans if needed
					logger.info("Cleaning up prototype beans on shutdown");
				}));
			}
		};
	}
}

