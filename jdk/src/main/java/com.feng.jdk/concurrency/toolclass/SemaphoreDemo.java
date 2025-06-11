package com.feng.jdk.concurrency.toolclass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class SemaphoreDemo {
    final static int SIZE = 25;

    public static void main(String[] args) throws Exception {
        final Pool<Fat> fatPool = new Pool<>(Fat.class, SIZE);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < SIZE; i++) {
            threadPool.execute(new CheckoutTask<>(fatPool));
        }
        print("All CheckoutTasks created");
        TimeUnit.SECONDS.sleep(200);
        
        List<Fat> list = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            Fat f = fatPool.checkOut();
            printnb(i + ": main() thread checked out ");
            f.operation();
            list.add(f);
        }
        Future<?> blocked = threadPool.submit(() -> {
            try { // Semaphore prevents additional checkout,
                // so call is blocked:
                fatPool.checkOut();
            } catch (InterruptedException e) {
                print("checkOut() Interrupted");
            }
        });
        TimeUnit.SECONDS.sleep(2);
        blocked.cancel(true); // Break out of blocked call
        print("Checking in objects in " + list);
        for (Fat f : list) {
            fatPool.checkIn(f);
        }
        for (Fat f : list) {
            fatPool.checkIn(f); // Second checkIn ignored
        }
        threadPool.shutdown();
    }


    // A task to check a resource out of a pool:
    static class CheckoutTask<T> implements Runnable {
        private static int counter = 0;
        private final int id = counter++;
        private Pool<T> pool;

        public CheckoutTask(Pool<T> pool) {
            this.pool = pool;
        }

        @Override
        public void run() {
            try {
                T item = pool.checkOut();
                print(this + "checked out " + item);
                TimeUnit.SECONDS.sleep(1);
                print(this + "checking in " + item);
                pool.checkIn(item);
            } catch (InterruptedException e) {
                // Acceptable way to terminate
            }
        }

        @Override
        public String toString() {
            return "CheckoutTask " + id + " ";
        }
    }

    static class Fat {
        private volatile double d; // Prevent optimization
        private static int counter = 0;
        private final int id = counter++;

        public Fat() { // Expensive, interruptible operation:
            for (int i = 1; i < 10000; i++) {
                d += (Math.PI + Math.E) / (double) i;
            }
        }

        public void operation() {
            System.out.println("operation " + this);
        }

        @Override
        public String toString() {
            return "Fat id: " + id;
        }
    }

    public static class Pool<T> {
        private int size;
        private List<T> items = new ArrayList<T>();
        private volatile boolean[] checkedOut;
        private Semaphore available;

        public Pool(Class<T> classObject, int size) {
            this.size = size;
            checkedOut = new boolean[size];
            available = new Semaphore(size, true);
            // Load pool with objects that can be checked out:
            for (int i = 0; i < size; ++i) {
                try {
                    // Assumes a default constructor:
                    items.add(classObject.newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public T checkOut() throws InterruptedException {
            available.acquire();
            return getItem();
        }

        public void checkIn(T x) {
            if (releaseItem(x)) {
                available.release();
            }
        }

        private synchronized T getItem() {
            for (int i = 0; i < size; ++i) {
                if (!checkedOut[i]) {
                    checkedOut[i] = true;
                    return items.get(i);
                }
            }
            return null; // Semaphore prevents reaching here
        }

        private synchronized boolean releaseItem(T item) {
            int index = items.indexOf(item);
            if (index == -1) {
                return false; // Not in the list
            }
            if (checkedOut[index]) {
                checkedOut[index] = false;
                return true;
            }
            return false; // Wasn’t checked out
        }
    }
}