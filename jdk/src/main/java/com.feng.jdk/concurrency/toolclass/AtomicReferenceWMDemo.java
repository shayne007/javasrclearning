package com.feng.jdk.concurrency.toolclass;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/12/21
 */

public class AtomicReferenceWMDemo {
    class WMRange {
        final int upper;
        final int lower;

        WMRange(int upper, int lower) {
            this.upper = upper;
            this.lower = lower;
        }
    }

    final AtomicReference<WMRange> wmRangeRef = new AtomicReference<>(
            new WMRange(0, 0)
    );

    // 设置库存上限
    void setUpper(int v) {
        WMRange newRef;
        WMRange oldRef;
        do {
            oldRef = wmRangeRef.get();
            // 检查参数合法性
            if (v < oldRef.lower) {
                throw new IllegalArgumentException();
            }
            newRef = new WMRange(v, oldRef.lower);
        } while (!wmRangeRef.compareAndSet(oldRef, newRef));
    }
}

