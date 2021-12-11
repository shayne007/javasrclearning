package com.feng.jdk.concurrency.simulation.restaurant;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class Plate {
    private final Order order;
    private final Food food;

    public Plate(Order ord, Food f) {
        order = ord;
        food = f;
    }

    public Order getOrder() {
        return order;
    }

    public Food getFood() {
        return food;
    }

    @Override
    public String toString() {
        return food.toString();
    }

}
