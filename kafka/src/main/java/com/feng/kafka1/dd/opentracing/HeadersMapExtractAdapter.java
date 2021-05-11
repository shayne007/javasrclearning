package com.feng.kafka1.dd.opentracing;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import io.opentracing.propagation.TextMap;

/**
 * @author fengsy
 * @date 3/22/21
 * @Description
 */
public class HeadersMapExtractAdapter implements TextMap {

    private final Map<String, String> map = new HashMap<>();

    public HeadersMapExtractAdapter(Headers headers) {
        for (Header header : headers) {
            byte[] headerValue = header.value();
            map.put(header.key(), headerValue == null ? null : new String(headerValue, StandardCharsets.UTF_8));
        }
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public void put(String key, String value) {
        throw new UnsupportedOperationException("HeadersMapExtractAdapter should only be used with Tracer.extract()");
    }
}
