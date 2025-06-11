package com.feng.jdk.concurrency.terminating;

import java.util.concurrent.TimeUnit;

import static net.mindview.util.Print.print;

/**
 * @author fengsy
 * @date 5/13/21
 * @Description
 */
public class InterruptingIdiom {
    public static void main(String[] args) throws Exception {

        Thread t = new Thread(new Blocked3());
        t.start();
        TimeUnit.MILLISECONDS.sleep(new Integer(5000));
        t.interrupt();
    }
}

class NeedsCleanup {
    private final int id;

    public NeedsCleanup(int ident) {
        id = ident;
        print("NeedsCleanup " + id);
    }

    public void cleanup() {
        print("Cleaning up " + id);
    }
}

class Blocked3 implements Runnable {
    private volatile double data = 0.0;

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // point1
                NeedsCleanup n1 = new NeedsCleanup(1);
                // Start try-finally immediately after definition
                // of n1, to guarantee proper cleanup of n1:
                try {
                    print("Sleeping");
                    TimeUnit.SECONDS.sleep(1);
                    // point2
                    NeedsCleanup n2 = new NeedsCleanup(2);
                    // Guarantee proper cleanup of n2:
                    try {
                        print("Calculating");
                        // A time-consuming, non-blocking operation:
                        for (int i = 1; i < 2500000; i++) {
                            data = data + (Math.PI + Math.E) / data;
                        }
                        print("Finished time-consuming operation");
                    } finally {
                        n2.cleanup();
                    }
                } finally {
                    n1.cleanup();
                }
            }
            print("Exiting via while() test");
        } catch (InterruptedException e) {
            print("Exiting via InterruptedException");
        }
    }
}