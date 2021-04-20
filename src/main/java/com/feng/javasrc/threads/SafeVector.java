package com.feng.javasrc.threads;

import java.util.Vector;

/**
 * @author fengsy
 * @date 1/13/21
 * @Description
 */

class SafeVector {
    private Vector v;

    // 所有公共方法增加同步控制
    synchronized void addIfNotExist(Object o) {
        if (!v.contains(o)) {
            v.add(o);
        }
    }
}