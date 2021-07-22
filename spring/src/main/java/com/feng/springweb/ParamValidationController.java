package com.feng.springweb;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */
@RestController
@Slf4j
@Validated
public class ParamValidationController {

    @RequestMapping(path = "students/{id}", method = RequestMethod.DELETE)
    public void deleteStudent(@PathVariable("id") @Range(min = 1, max = 10000) String id) {
        log.info("delete student: {}", id);
        // 省略业务代码
    }

    @RequestMapping(path = "students", method = RequestMethod.POST)
    public void addStudent(@RequestBody @Validated Student student) {
        log.info("add new student: {}", student.toString());
        // 省略业务代码
    };

    @Data
    public static class Student {
        @NotNull
        @Size(min = 1, max = 10)
        private String name;
        private Integer age;
        @Valid
        private Phone phone;
    }

    @Data
    static class Phone {
        @Size(max = 11)
        private String number;
    }
}
