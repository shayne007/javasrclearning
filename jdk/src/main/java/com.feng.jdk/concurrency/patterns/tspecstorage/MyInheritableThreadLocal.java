package com.feng.jdk.concurrency.patterns.tspecstorage;

import com.alibaba.fastjson.JSONObject;

/**
 * 继承InheritableThreadLocal 使用 序列化和反序列化将value进行深拷贝
 *
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public class MyInheritableThreadLocal<T> extends InheritableThreadLocal<T> {
    @Override
    protected T childValue(T parentValue) {
        String s = JSONObject.toJSONString(parentValue);
        return (T) JSONObject.parseObject(s, parentValue.getClass());
    }
}
