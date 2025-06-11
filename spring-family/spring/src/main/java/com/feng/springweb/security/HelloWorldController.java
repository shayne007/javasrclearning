package com.feng.springweb.security;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

@RestController
public class HelloWorldController {
    @RequestMapping(path = "admin", method = RequestMethod.GET)
    public String admin() {
        return "admin operation";
    }

    @RequestMapping(path = "login", method = RequestMethod.GET)
    public String login() {
        return "login operation";
    }
}
