package com.fsy.javasrc.basic.string;

/**
 * @author fengsy
 * @date 1/30/21
 * @Description
 */
public final class MyString {
    private final byte[] value;
    private int hash;

    public MyString() {
        value = "".getBytes();
    }

    public MyString(final String str) {
        value = str.getBytes();
    }

    public MyString(final char[] chars) {
        byte[] val = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            val[i] = (byte)chars[i];
        }
        value = val;
    }

    @Override
    public boolean equals(final Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof MyString) {
            MyString aString = (MyString)anObject;
            return equals(value, aString.value);
        }
        return false;
    }

    private boolean equals(final byte[] value, final byte[] other) {
        if (value.length == other.length) {
            int len = value.length;
            for (int i = 0; i < len; i++) {
                if (value[i] != other[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 0;
        int length = value.length >> 1;
        for (int i = 0; i < length; i++) {
            h = 31 * h + value[i];
        }
        return h;
    }

    public boolean isEmpty() {
        return value.length == 0;
    }

    public static void main(String[] args) {
        MyString str = new MyString();
        System.out.println(str.isEmpty());
        MyString str1 = new MyString("str");
        MyString str2 = new MyString("str");
        char[] chars = {'s', 't', 'r'};
        MyString str3 = new MyString(chars);
        System.out.println(str1.equals(str2));
        System.out.println(str3.equals(str2));
        System.out.println(str1 == str2);

    }
}
