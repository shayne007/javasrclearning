package com.feng.springweb.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */
@Controller
@Slf4j
public class StudentController {
    public StudentController() {
        log.info("construct");
    }

    @PostMapping("/regStudent/{name}")
    @ResponseBody
    public String saveUser(@PathVariable String name) throws Exception {
        log.info("{} 用户注册成功", name);
        // throw new NotAllowException("token not valid");
        return "success";
    }
}
