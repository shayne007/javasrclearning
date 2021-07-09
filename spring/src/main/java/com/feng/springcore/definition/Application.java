package com.feng.springcore.definition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public String serviceName() {
        return "MyServiceName";
    }

    @Bean
    @Order(1)
    public Student student1() {
        return createStudent(111, "xie");
    }

    @Bean
    public Student student2() {
        return createStudent(22, "fang");
    }

    /**
     * 存在第一种Bean注入方式的情况下，此种方式不生效
     *
     * @return
     */
    @Bean
    public List<Student> students() {
        Student student3 = createStudent(3, "liu");
        Student student4 = createStudent(4, "fu");
        return Arrays.asList(student3, student4);
    }

    private Student createStudent(int id, String name) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        return student;
    }
}