package com.feng.jdk.arrayandlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fengsy
 * @date 5/11/21
 * @Description
 */
public class ContainerComparison {
    public static void main(String[] args) {
        BerylliumSphere[] spheres = new BerylliumSphere[10];
        for (int i = 0; i < 5; i++) {
            spheres[i] = new BerylliumSphere();
        }
        System.out.println(Arrays.toString(spheres));
        System.out.println(spheres[4]);

        List<BerylliumSphere> berylliumSpheres = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            berylliumSpheres.add(new BerylliumSphere());
        }
        System.out.println(berylliumSpheres);
        System.out.println(berylliumSpheres.get(4));

        int[] ints = new int[]{1, 23, 4, 5, 5};
        System.out.println(ints);
        System.out.println(ints[4]);

        List<Integer> list = new ArrayList<>(Arrays.asList(22));
        list.addAll(Arrays.asList(1, 4, 33));
        list.add(3);
        System.out.println(list);
        System.out.println(list.get(4));
    }
}

class BerylliumSphere {
    private static long counter;
    private final long id = counter++;

    @Override
    public String toString() {
        return "Sphere " + id;
    }
}