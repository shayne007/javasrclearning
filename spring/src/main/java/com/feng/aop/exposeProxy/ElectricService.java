package com.feng.aop.exposeProxy;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */
@Service
public class ElectricService {
    // @Autowired
    // ElectricService electricService;

    public void charge() throws Exception {
        System.out.println("Electric charging ...");
        // this.pay();//aop invalid

        ElectricService electricService = ((ElectricService)AopContext.currentProxy());
        electricService.pay();
    }

    public void pay() throws Exception {
        System.out.println("Pay with alipay ...");
        Thread.sleep(1000);
    }

}