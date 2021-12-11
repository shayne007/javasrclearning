package com.feng.jdk.concurrency.simulation.restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class Course {
    private static List<Course> courses;
    private Random random = new Random(47);

    public static List<Course> values() {
        return new ArrayList<>(12);
    }

    public Food randomSelection() {
        return new Food(random.nextInt(10));
    }
}
