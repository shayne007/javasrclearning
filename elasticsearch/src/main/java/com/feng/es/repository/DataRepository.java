package com.feng.es.dao;

import com.feng.es.util.ElasticsearchUtil;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 12/2/21
 */
public class DataRepository {

    public <T> Map<Integer, List<T>> batchInsert(Map<String, String> map, Class<T> tClass) {
        ElasticsearchUtil
    }

    public <T> List<T> query(String idName, String id, Class<T> tClass) {
        return null;
    }
}
