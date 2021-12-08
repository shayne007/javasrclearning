package com.feng.concurrency.patterns.tspecstorage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public class MyTransmittableThreadLocal<T> extends TransmittableThreadLocal<T> {
    @Override
    public T copy(T parentValue) {
        String s = JSONObject.toJSONString(parentValue);
        return (T)JSONObject.parseObject(s, parentValue.getClass());
    }
}
