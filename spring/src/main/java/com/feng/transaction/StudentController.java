package com.feng.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */
@RestController
@Slf4j
public class StudentController {
    @Autowired
    private StudentService service;

    @GetMapping("/stu")
    public String save() throws Exception {
        service.saveStudent("小明");
        return "success";
    }

}
