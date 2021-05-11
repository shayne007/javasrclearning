package com.feng.oop;

/**
 * @author fengsy
 * @date 1/30/21
 * @Description
 */
class Memory {
    public void m() {
        System.out.println("Memory");
    }
}

class CPU {
    public void cpu() {
        System.out.println("CPU");
    }
}

class Computer {
    class CPUInner extends CPU {}

    class MemoruInner extends Memory {}

    public void m() {
        new MemoruInner().m();
    }

    public void cpu() {
        new CPUInner().cpu();
    }

}

public class TestInnerClass {
    public static void main(String[] args) {
        Computer computer = new Computer();
        computer.cpu();
        computer.m();
    }
}
