package com.feng.aop.proxyAttributeUnreachable;

import com.feng.aop.exposeProxy.ElectricService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */
@Service
public class AdminElectricService {

    AdminUserService userService;

    public AdminElectricService(AdminUserService userService) {
        this.userService = userService;
    }

    public void charge() throws Exception {
        AdminElectricService adminElectricService = ((AdminElectricService)AopContext.currentProxy());
        adminElectricService.doCharge();
    }

    public void doCharge() throws Exception {
        System.out.println("Electric charging ...");
        this.pay();
    }

    public void pay() throws Exception {
        userService.login();

        // String payNum = adminUserService.adminUser.getPayNum();
        String payNum = userService.getUser().getPayNum();
        System.out.println("User pay num : " + payNum);

        System.out.println("Pay with alipay ...");
        Thread.sleep(1000);
    }

}