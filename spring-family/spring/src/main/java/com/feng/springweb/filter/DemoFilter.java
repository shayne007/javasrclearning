package com.feng.springweb.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

@Component
@Slf4j
public class DemoFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        try {
            // 模拟异常
            System.out.println("Filter 处理中时发生异常");
            throw new RuntimeException();
        } catch (Exception e) {
            // chain.doFilter(request, response);
        }
        chain.doFilter(request, response);
    }
}