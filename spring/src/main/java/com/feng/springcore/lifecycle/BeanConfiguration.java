package com.feng.springcore.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengsy
 * @date 7/8/21
 * @Description
 */

@Configuration
public class BeanConfiguration {
    /**
     * 未指定时默认的destroyMethod是shutdown
     * 
     * @return
     */
    @Bean(destroyMethod = "")
    public LightService getTransmission() {
        return new LightService();
    }
}