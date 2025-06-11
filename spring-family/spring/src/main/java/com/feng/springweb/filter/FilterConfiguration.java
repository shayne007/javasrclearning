package com.feng.springweb.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

@Configuration
public class FilterConfiguration {
    @Bean
    public FilterRegistrationBean authFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean timeCostFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new TimeCostFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
