package com.feng.javasrc.basic.generic;

/**
 * @author fengsy
 * @date 2/20/21
 * @Description
 */
public class InstanceFactory {
    static <T extends Parent> T build(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Parent p1 = InstanceFactory.build(Parent.class);
        Parent p2 = InstanceFactory.build(Sub1.class);
        Sub1 sub1 = InstanceFactory.build(Sub1.class);
        Parent sub2 = InstanceFactory.build(Sub2.class);
    }
}

class Parent {}

class Sub1 extends Parent {}

class Sub2 extends Sub1 {}
