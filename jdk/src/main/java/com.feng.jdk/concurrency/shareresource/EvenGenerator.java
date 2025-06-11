package com.feng.jdk.concurrency.shareresource;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class EvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    /**
     * It’s possible for one task to call next( ) after another task has performed the first increment of
     * currentEvenValue but not the second. a
     *
     * @return a int value suppose to an even
     */
    @Override
    public int next() {

        // Danger point here!
        ++currentEvenValue;
        Thread.yield();
        ++currentEvenValue;
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenGenerator());
    }
}
