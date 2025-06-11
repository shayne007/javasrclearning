package com.feng.jdk.concurrency.toolclass;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @Description Fork/Join：单机版的MapReduce
 * 分而治之的思想；
 * @Author fengsy
 * @Date 11/8/21
 */
public class ForkJoinDemo {

    public static void main(String[] args) {
        //创建分治任务线程池
        ForkJoinPool fjp = new ForkJoinPool(Runtime.getRuntime().availableProcessors() + 1);
        //创建分治任务
        FibonacciTask fib = new FibonacciTask(30);
        //启动分治任务
        Integer result = fjp.invoke(fib);
        //输出结果
        System.out.println(result);
    }

    //递归任务
    static class FibonacciTask extends RecursiveTask<Integer> {
        final int n;

        FibonacciTask(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n <= 1)
                return n;
            FibonacciTask f1 = new FibonacciTask(n - 1);
            //创建子任务
            f1.fork();
            FibonacciTask f2 = new FibonacciTask(n - 2);
            //等待子任务结果，并合并结果
            return f2.compute() + f1.join();
        }
    }
}
