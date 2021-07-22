package com.feng.springweb;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */

@RestController
@Slf4j
public class WebURLController {

    @RequestMapping(path = "/hi4", method = RequestMethod.GET)
    public String hi4(@RequestParam("name") String name,
        @RequestParam(name = "address", defaultValue = "DEFAULT", required = false) Optional address) {
        return name + ":" + address;
    }

    @RequestMapping(path = "/hi1", method = RequestMethod.GET)
    public String hi1(@RequestHeader() HttpHeaders map) {
        return map.toString();
    }

    @RequestMapping(path = "/hi2", method = RequestMethod.GET)
    public String hi2(@RequestHeader("MyHeader") String myHeader) {
        return myHeader;
    }

    @RequestMapping(path = "/hi3", method = RequestMethod.GET)
    public String hi3(@RequestHeader("MyHeader") String myHeader, @RequestHeader MultiValueMap map) {
        return myHeader + " compare with : " + map.get("MyHeader");
    };
}