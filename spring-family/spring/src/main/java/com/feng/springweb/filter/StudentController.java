package com.feng.springweb.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

@RestController
@Slf4j
public class StudentController {
    @PostMapping("/regStudent/{name}")
    public String saveUser(@PathVariable String name) {
        log.info("用户: {}, 注册成功", name);
        return "success";
    }
}
