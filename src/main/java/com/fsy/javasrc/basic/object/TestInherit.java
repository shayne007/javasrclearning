package com.fsy.javasrc.basic.object;

/**
 * @author fengsy
 * @date 1/30/21
 * @Description
 */
interface CanRun {
    void run();
}

interface CanFly {
    void fly();
}

class Animal {
    public int i = 1;

    Animal() {
        System.out.println("Animal: " + this.getClass().getName());
        getCategory();
    }

    public void getCategory() {
        System.out.println("Im a animal.");
    }
}

class Duck extends Animal implements CanFly, CanRun {
    public int i = 2;

    @Override
    public void getCategory() {
        System.out.println("Im a duck.");
    }

    @Override
    public void run() {
        System.out.println("running...");
    }

    @Override
    public void fly() {
        System.out.println("flying...");
    }
}

public class TestInherit {
    public static void main(String[] args) {
        Animal a = new Duck();
        a.getCategory();
        ((Duck)a).run();
        ((Duck)a).fly();
        System.out.println(a.i);
        System.out.println("-------------------");
        Duck d = new Duck();
        d.getCategory();
        d.run();
        d.fly();
        System.out.println(d.i);
        System.out.println("-------------------");
        new Animal();
    }

}
