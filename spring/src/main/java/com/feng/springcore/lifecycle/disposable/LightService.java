package com.feng.springcore.lifecycle.disposable;

import java.io.Closeable;
import java.io.IOException;

import org.springframework.stereotype.Service;

/**
 * Service 标记的 LightService 也没有实现 AutoCloseable、DisposableBean，最终没有添加一个
 * DisposableBeanAdapter。
 *
 * 所以最终我们定义的 shutdown 方法没有被调用。
 *
 * @author fengsy
 * @date 7/8/21
 * @Description
 */

@Service
public class LightService {

	public void start() {
		System.out.println("turn on all lights");
	}

	public void shutdown() {
		System.out.println("turn off all lights by shutdown()");
	}

	public void check() {
		System.out.println("check all lights");
	}

	public void close() throws IOException {
		System.out.println("turn off all lights by close()");
		shutdown();
	}
}