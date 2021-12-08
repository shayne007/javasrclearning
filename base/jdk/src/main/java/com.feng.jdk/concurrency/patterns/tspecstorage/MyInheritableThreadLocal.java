package com.feng.concurrency.patterns.tspecstorage;

import com.alibaba.fastjson.JSONObject;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public class MyInheritableThreadLocal<T> extends InheritableThreadLocal<T> {
    @Override
    protected T childValue(T parentValue) {
        String s = JSONObject.toJSONString(parentValue);
        return (T)JSONObject.parseObject(s, parentValue.getClass());
    }
}
