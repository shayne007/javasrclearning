package com.feng.order.util;

import com.google.gson.Gson;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
public class JsonUtil {
    private static final Gson GSON = new Gson();

    private JsonUtil() {}

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        return GSON.fromJson(jsonStr, clazz);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }
}
