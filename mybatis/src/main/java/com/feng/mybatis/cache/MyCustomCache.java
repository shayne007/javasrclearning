package com.feng.mybatis.cache;

import org.apache.ibatis.cache.impl.PerpetualCache;

/**
 * @author fengsy
 * @date 7/26/21
 * @Description
 */
public class MyCustomCache extends PerpetualCache {
    private String cacheID;

    public MyCustomCache(String id) {
        super(id);
    }

    public String getCacheID() {
        return cacheID;
    }

    public void setCacheID(String cacheID) {
        this.cacheID = cacheID;
    }
}
