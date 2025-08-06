package com.feng.springcore.lifecycle.extension;

import javax.annotation.PostConstruct;

/**
 * TODO
 *
 * @since 2025/8/6
 */
public class Hello {

	@PostConstruct
	public void init() {
		System.out.println("Hello constructor");
	}

	public String hello() {
		return "hello";
	}
}

