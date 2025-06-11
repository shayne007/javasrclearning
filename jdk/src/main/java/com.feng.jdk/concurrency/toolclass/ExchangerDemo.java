package com.feng.jdk.concurrency.toolclass;

import net.mindview.util.BasicGenerator;
import net.mindview.util.Generator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class ExchangerDemo {
    static int size = 10;
    static int delay = 5; // Seconds

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            size = new Integer(args[0]);
        }
        if (args.length > 1) {
            delay = new Integer(args[1]);
        }
        ExecutorService pool = Executors.newCachedThreadPool();
        Exchanger<List<Fat>> exchanger = new Exchanger<>();
        List<Fat> producerList = new CopyOnWriteArrayList<>();
        List<Fat> consumerList = new CopyOnWriteArrayList<>();
        pool.execute(new ExchangerProducer<>(exchanger, BasicGenerator.create(Fat.class), producerList));
        pool.execute(new ExchangerConsumer<>(exchanger, consumerList));
        TimeUnit.SECONDS.sleep(delay);
        pool.shutdownNow();
    }

    public static class Fat {
        private volatile double d; // Prevent optimization
        private static int counter = 0;
        private final int id = counter++;

        public Fat() { // Expensive, interruptible operation:
            for (int i = 1; i < 10000; i++) {
                d += (Math.PI + Math.E) / (double) i;
            }
        }

        public void operation() {
            System.out.println(this);
        }

        @Override
        public String toString() {
            return "Fat id: " + id;
        }
    }


    static class ExchangerProducer<T> implements Runnable {
        private Generator<T> generator;
        private Exchanger<List<T>> exchanger;
        private List<T> holder;

        ExchangerProducer(Exchanger<List<T>> exchg, Generator<T> gen, List<T> holder) {
            exchanger = exchg;
            generator = gen;
            this.holder = holder;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    for (int i = 0; i < ExchangerDemo.size; i++) {
                        holder.add(generator.next());
                    }
                    // Exchange full for empty:
                    holder = exchanger.exchange(holder);
                }
            } catch (InterruptedException e) {
                // OK to terminate this way.
            }
        }
    }

    static class ExchangerConsumer<T> implements Runnable {
        private Exchanger<List<T>> exchanger;
        private List<T> holder;
        private volatile T value;

        ExchangerConsumer(Exchanger<List<T>> ex, List<T> holder) {
            exchanger = ex;
            this.holder = holder;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    holder = exchanger.exchange(holder);

                    for (T x : holder) {
                        value = x; // Fetch out value
                        holder.remove(x); // OK for CopyOnWriteArrayList
                    }
                }
            } catch (InterruptedException e) {
                // OK to terminate this way.
            }
            System.out.println("Final value: " + value);
        }
    }
}