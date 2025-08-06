package com.feng.springcore.lifecycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.feng.springcore.lifecycle.extension.Hello;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */

@Configuration
@Slf4j
public class Application {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext =
				new AnnotationConfigApplicationContext(Application.class);
		applicationContext.start(); // 这会触发Lifecycle的start()
		Hello hello = applicationContext.getBean("hello", Hello.class);
		System.out.println(hello.hello());
		applicationContext.close(); // 这会触发Lifecycle的stop()
		System.out.println(hello.hello());
	}

	@Bean
	public Hello hello() {
		return new Hello();
	}

}