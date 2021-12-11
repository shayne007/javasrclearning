package com.feng.jvm;

import java.util.HashSet;
import java.util.Set;

/**
 * @author fengsy
 * @date 1/26/21
 * @Description VM Args： jdk6: -XX:PermSize=6M -XX:MaxPermSize=6M jdk7: -XX:MaxPermSize=6M jdk8: -XX:MaxMetaspaceSize=6M
 */
public class RuntimeConstantPoolOOM_1 {

    public static void main(String[] args) {
        // 使用Set保持着常量池引用，避免Full GC回收常量池行为
        Set<String> set = new HashSet<String>();
        // 在short范围内足以让6MB的PermSize产生OOM了
        short i = 0;
        while (true) {
            set.add(String.valueOf(i++).intern());
        }
    }
}
