package com.feng.http;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengsy
 * @date 7/13/21
 * @Description
 */
@RestController
public class HelloWorldController {
    @RequestMapping(path = "hi", method = RequestMethod.POST)
    public String hi(@RequestParam("para1") String para1, @RequestParam("para2") String para2) {
        return "helloworld:" + para1 + "," + para2;
    };

    @RequestMapping(path = "hi2", method = RequestMethod.GET)
    public String hi(@RequestParam("para") String para) {
        return "helloworld:" + para;
    };
}
