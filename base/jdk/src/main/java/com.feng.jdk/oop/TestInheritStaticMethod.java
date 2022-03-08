package com.feng.jdk.oop;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 2/17/22
 */
public class TestInheritStaticMethod {
    public static void superStaticPrint() {
        System.out.println("super static do.");
    }

    public void superPrint() {
        System.out.println("super do.");
    }

    public static class ChildClass extends TestInheritStaticMethod {
        public static void superStaticPrint() {
            System.out.println("child static do.");
        }

        @Override
        public void superPrint() {
            System.out.println("child do.");
        }
    }


    public static void main(String[] args) {
        TestInheritStaticMethod test = new ChildClass();
        superStaticPrint();
        test.superPrint();

    }
    
}

