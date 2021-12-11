package com.feng.jdk.string;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fengsy
 * @date 1/30/21
 * @Description
 */
final class ImmutableClass {
    private final Date d;

    public ImmutableClass(Date d) {
        this.d = (Date) d.clone();
    }

    public void printState() {
        System.out.println(d);
    }

    public Date getDate() {
        return (Date) d.clone();
    }

    @Override
    public int hashCode() {
        return d.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImmutableClass) {
            return ((ImmutableClass) obj).d.equals(d);
        } else {
            return false;
        }

    }

    @Override
    public String toString() {
        return d.toString();
    }

}

public class TestImmutable {
    public static void main(String[] args) {
        Date d = new Date();
        ImmutableClass immutableClass = new ImmutableClass(d);
        ImmutableClass immutableClass2 = new ImmutableClass(d);
        System.out.println(immutableClass.equals(immutableClass2));
        immutableClass.printState();
        d.setMonth(5);
        immutableClass.printState();
        Set set = new HashSet<ImmutableClass>(2);
        set.add(immutableClass);
        set.add(immutableClass2);
        System.out.println(set.size());
        set.stream().forEach((s) -> System.out.println(s.toString()));

    }

}