package com.feng.springweb.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Service;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */
@Service
@Slf4j
public class MetricsService {
    @Autowired
    // @Qualifier("com.feng.springweb.filter.TimeCostFilter")
    FilterRegistrationBean timeCostFilter;

    public void saveMetrics() {
        TimeCostFilter filter = (TimeCostFilter)timeCostFilter.getFilter();
        log.info(filter.getResult().toString());
    }
}
