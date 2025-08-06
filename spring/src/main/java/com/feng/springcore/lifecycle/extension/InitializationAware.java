package com.feng.springcore.lifecycle.extension;

// Supporting interfaces
public interface InitializationAware {

	void beforeInitialization();

	void afterInitialization();
}
