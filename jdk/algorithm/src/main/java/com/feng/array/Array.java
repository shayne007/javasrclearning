package com.feng.array;

/**
 * @author fengsy
 * @date 6/12/21
 * @Description
 */
public class Array {
    public int data[];
    private int n;
    private int count;

    public Array(int capacity) {
        this.data = new int[capacity];
        this.n = capacity;
        this.count = 0;
    }

    public int find(int index) {
        if (index < 0 || index >= count) {
            return -1;
        } else {
            return data[index];
        }
    }

    /**
     * 插入数据时，移动数组的元素从末尾开始往前
     * 
     * @param index
     * @param value
     * @return
     */
    public boolean insert(int index, int value) {
        if (n == count) {
            System.out.println("no space to insert a value");
            return false;
        }
        if (index < 0 || index > count) {
            System.out.println("illegal array index");
            return false;
        }
        for (int i = count; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = value;
        ++count;
        return true;
    }

    /**
     * 删除数据时，移动数组中的元素从前往后
     * 
     * @param index
     * @return
     */

    public boolean delete(int index) {
        if (index < 0 || index >= count) {
            System.out.println("illegal array index");
            return false;
        }
        for (int i = index + 1; i < count; i++) {
            data[i - 1] = data[i];
        }
        --count;
        return true;
    }

    public void printAll() {
        for (int i = 0; i < count; ++i) {
            System.out.print(data[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Array array = new Array(5);
        array.printAll();
        array.insert(0, 3);
        array.insert(0, 4);
        array.insert(1, 5);
        array.insert(3, 9);
        array.insert(3, 10);
        array.printAll();
        array.delete(2);
        array.printAll();
    }
}
