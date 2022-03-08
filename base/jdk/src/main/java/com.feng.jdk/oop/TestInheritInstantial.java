package com.feng.jdk.oop;

/**
 * @Description 父类实例化过程中调用的方法被子类重写，此时子类未实例化
 * @Author fengsy
 * @Date 2/17/22
 */
public class TestInheritInstantial {

    private String name = "super";

    public TestInheritInstantial() {
        print();
    }

    public void print() {
        System.out.println(name);
    }

    static class ChildClass extends TestInheritInstantial {
        private String name = "child";

        @Override
        public void print() {
            System.out.println(name);
        }
    }

    public static void main(String[] args) {
        TestInheritInstantial test = new ChildClass();
        test.print();
    }
}
