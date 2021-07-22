package com.feng.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.feng.transaction.mapper.CourseMapper;
import com.feng.transaction.mapper.StudentCourseMapper;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentCourseMapper studentCourseMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void regCourse(Cache cache, int studentId) throws Exception {
        new TransactionAwareCacheDecorator(cache).put(studentId, 1);
        studentCourseMapper.saveStudentCourse(studentId, 1);
        courseMapper.addCourseNumber(1);
        throw new Exception("注册失败");
    }
}
