package com.feng.springcore.definition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fengsy
 * @date 7/8/21
 * @Description
 */

@RestController
@Slf4j
public class StudentController {

    private List<Student> students;

    public StudentController(List<Student> students) {
        this.students = students;
    }

    @RequestMapping(path = "students", method = RequestMethod.GET)
    public String listStudents() {
        return students.toString();
    };

}