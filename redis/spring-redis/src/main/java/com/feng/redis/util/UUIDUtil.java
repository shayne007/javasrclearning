package com.feng.redis.util;

import java.util.UUID;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/23/21
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
