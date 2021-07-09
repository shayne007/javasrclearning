package com.feng.aop;

import com.feng.aop.exposeProxy.ElectricService;
import com.feng.aop.proxyAttributeUnreachable.AdminElectricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */
@RestController
public class ElectricController {
    @Autowired
    ElectricService electricService;
    @Autowired
    AdminElectricService adminElectricService;

    @RequestMapping(path = "charge", method = RequestMethod.GET)
    public void charge() throws Exception {
        electricService.charge();
    };

    @RequestMapping(path = "admincharge", method = RequestMethod.GET)
    public void admincharge() throws Exception {
        adminElectricService.charge();
    };
}