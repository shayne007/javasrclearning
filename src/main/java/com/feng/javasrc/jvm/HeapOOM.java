package com.feng.javasrc.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengsy
 * @date 1/26/21
 * @Description VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */

public class HeapOOM {

    static class OOMObject {}

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();

        while (true) {
            list.add(new OOMObject());
        }
    }
}
