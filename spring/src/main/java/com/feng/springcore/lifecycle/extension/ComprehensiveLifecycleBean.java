package com.feng.springcore.lifecycle.extension;

/**
 * TODO
 *
 * @since 2025/8/6
 */
public class ComprehensiveLifecycleBean {

	public void customInitMethod() {
		System.out.println("执行自定义初始化方法");
	}

	public void customDestroyMethod() {
		System.out.println("执行自定义销毁方法");
	}

}
