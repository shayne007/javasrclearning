package net.mindview.util;

import java.util.Random;

public class RandomGenerator {
    private static Random rand = new Random();
    public static class Integer implements Generator<java.lang.Integer> {
        public java.lang.Integer next() {
            return rand.nextInt();
        }
    }
    public static class String implements Generator<java.lang.String> {
        public java.lang.String next() {
            return Long.toHexString(rand.nextLong());
        }
    }
} 