package com.feng.springweb.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

// @WebFilter
@Slf4j
// @Order(1)
public class TimeCostFilter implements Filter {
    private Map<String, Long> result;

    public Map<String, Long> getResult() {
        return result;
    }

    public TimeCostFilter() {
        result = new HashMap<>();
        log.info("constructor executing in: " + TimeCostFilter.class.getName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        log.info("开始计算接口耗时");
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        long end = System.currentTimeMillis();
        long time = end - start;
        result.putIfAbsent("timecost", time);
        log.info(result.toString());
        log.info("执行时间(ms): {}", time);
    }
}