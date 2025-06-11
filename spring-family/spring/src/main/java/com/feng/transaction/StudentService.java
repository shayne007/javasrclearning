package com.feng.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feng.transaction.mapper.StudentMapper;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

@Service
public class StudentService {
    private Cache cache = new ConcurrentMapCache("myCache");
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    StudentService studentService;

    @Autowired
    CourseService courseService;

    @Transactional(rollbackFor = Exception.class)
    public void saveStudent(String realname) throws Exception {
        Student student = new Student();
        student.setId(11);
        student.setRealname(realname);

        studentService.doSave(student);

        try {
            courseService.regCourse(cache, student.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(cache.get(11));
    }

    public void doSave(Student student) throws Exception {
        studentMapper.saveStudent(student.getRealname());
        /*if (student.getRealname().equals("小明")) {
            throw new Exception("该学生已存在");
        }*/
    }
}
