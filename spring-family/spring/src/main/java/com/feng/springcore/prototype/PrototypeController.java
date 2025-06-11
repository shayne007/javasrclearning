package com.feng.springcore.prototype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengsy
 * @date 7/8/21
 * @Description
 */

@RestController
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PrototypeController {

    @Autowired
    private PrototypeService prototypeService;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 当一个单例的 Bean，使用 autowired 注解标记其属性时，你一定要注意这个属性值会被固定下来。
     * 
     * @return
     */
    @RequestMapping(path = "service", method = RequestMethod.GET)
    public String service() {
        return "helloworld, service is : " + prototypeService;
    };

    @RequestMapping(path = "prototype", method = RequestMethod.GET)
    public String prototype() {
        return "helloworld, service is : " + applicationContext.getBean(PrototypeService.class);
    };

    @RequestMapping(path = "lookup", method = RequestMethod.GET)
    public String lookup() {
        return "helloworld, service is : " + getServiceImpl();
    };

    /**
     * 使用Lookup后方法中的内容不会被执行
     * 
     * @return
     */
    @Lookup
    public PrototypeService getServiceImpl() {
        System.out.println("executing...");
        return null;
    }
}