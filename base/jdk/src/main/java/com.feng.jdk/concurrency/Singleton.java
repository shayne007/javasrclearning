package com.feng.concurrency;

/**
 * @author fengsy
 * @date 1/10/21
 * @Description
 */
public class Singleton {
    /**
     * volatile 保证可见性，禁止指令重排，刷新cpu缓存至内存
     */
    private static volatile Singleton instance;

    /**
     * 
     * @return 单实例对象
     */
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    // 对象的实例化不是原子操作，且被编译器优化后，先返回内存地址后实例化对象
                    instance = new Singleton();
                }
            }
        }

        return instance;
    }

}
