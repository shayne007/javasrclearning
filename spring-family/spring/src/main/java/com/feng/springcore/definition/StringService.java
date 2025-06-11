package com.feng.springcore.definition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author fengsy
 * @date 7/8/21
 * @Description
 */

@Service
public class StringService {
    private String serviceName;

    // public StringService(String serviceName) {
    // this.serviceName = serviceName;
    // System.out.println(serviceName);
    // }

    public StringService(String serviceName, String otherStringParameter) {
        this.serviceName = serviceName;
        System.out.println(serviceName);
        System.out.println(otherStringParameter);
    }

    /**
     * 此处产生了循环依赖, 将@Bean定义移至其他类，比如启动类Application中
     * 
     * The dependencies of some of the beans in the application context form a cycle
     * 
     * @return
     */
    // @Bean
    // public String serviceName() {
    // return "MyServiceName";
    // }
}