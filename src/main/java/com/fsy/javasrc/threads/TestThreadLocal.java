package com.fsy.javasrc.threads;

/**
 * @author fengsy
 * @date 1/13/21
 * @Description
 */

public class TestThreadLocal {
    /* private static final ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };*/

    private static final ThreadLocal<Integer> value = ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new TestThread(i)).start();
        }
    }

    static class TestThread implements Runnable {
        private int index;

        public TestThread(int index) {
            this.index = index;
        }

        public void run() {
            System.out.println("线程" + index + "的累加前:" + value.get());
            for (int i = 0; i < 5; i++) {
                value.set(value.get() + i);
            }
            System.out.println("线程" + index + "的累加后:" + value.get());
        }
    }
}