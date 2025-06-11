package com.feng.springtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengsy
 * @date 7/13/21
 * @Description
 */

@RestController
public class HelloController {

    @Autowired
    HelloWorldService helloWorldService;

    @RequestMapping(path = "hi", method = RequestMethod.GET)
    public String hi() throws Exception {
        return helloWorldService.toString();
    };

}