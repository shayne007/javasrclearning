package com.fsy.javasrc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/test0")
    public String test0(HttpServletRequest request) {
        ThreadLocal<Byte[]> localVariable = new ThreadLocal<Byte[]>();
        try {
            localVariable.set(new Byte[4096*1024]);// 为线程添加变量

            //

        }
        finally {
            localVariable.remove();
        }

        return "success";
    }

}