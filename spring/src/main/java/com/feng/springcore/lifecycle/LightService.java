package com.feng.springcore.lifecycle;

import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;

/**
 * Service 标记的 LightService 也没有实现 AutoCloseable、DisposableBean，最终没有添加一个 DisposableBeanAdapter。
 * 
 * 所以最终我们定义的 shutdown 方法没有被调用。
 * 
 * @author fengsy
 * @date 7/8/21
 * @Description
 */

@Service
public class LightService implements Closeable {
    public void start() {
        System.out.println("turn on all lights");
    }

    public void shutdown() {
        System.out.println("turn off all lights");
    }

    public void check() {
        System.out.println("check all lights");
    }

    @Override
    public void close() throws IOException {
        shutdown();
    }
}