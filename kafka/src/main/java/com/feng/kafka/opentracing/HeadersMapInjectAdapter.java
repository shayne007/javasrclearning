package com.feng.kafka.opentracing;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

import org.apache.kafka.common.header.Headers;

import io.opentracing.propagation.TextMap;

/**
 * @author fengsy
 * @date 3/22/21
 * @Description
 */
public class HeadersMapInjectAdapter implements TextMap {

    private final Headers headers;

    public HeadersMapInjectAdapter(Headers headers) {
        this.headers = headers;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        throw new UnsupportedOperationException("iterator should never be used with Tracer.inject()");
    }

    @Override
    public void put(String key, String value) {
        headers.add(key, value.getBytes(StandardCharsets.UTF_8));
    }
}
