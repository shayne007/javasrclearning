package net.mindview.util;

public class CountingGenerator {
    public static class Boolean implements Generator<java.lang.Boolean> {
        private boolean value = false;
        public java.lang.Boolean next() {
            value = !value;
            return value;
        }
    }

    public static class Integer implements Generator<java.lang.Integer> {
        private int value = 0;
        public java.lang.Integer next() {
            return value++;
        }
    }

    public static class Long implements Generator<java.lang.Long> {
        private long value = 0;
        public java.lang.Long next() {
            return value++;
        }
    }

    public static class Float implements Generator<java.lang.Float> {
        private float value = 0;
        public java.lang.Float next() {
            return value++;
        }
    }

    public static class Double implements Generator<java.lang.Double> {
        private double value = 0;
        public java.lang.Double next() {
            return value++;
        }
    }
} 