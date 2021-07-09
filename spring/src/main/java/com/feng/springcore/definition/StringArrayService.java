package com.feng.springcore.definition;

import org.springframework.stereotype.Service;

/**
 * @author fengsy
 * @date 7/8/21
 * @Description
 */

@Service
public class StringArrayService {

    private String[] serviceNames;

    public StringArrayService(String[] serviceNames) {
        this.serviceNames = serviceNames;
        System.out.println(serviceNames);
    }

}