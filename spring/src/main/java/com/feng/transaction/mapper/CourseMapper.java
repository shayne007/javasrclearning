package com.feng.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */
@Mapper
public interface CourseMapper extends BaseMapper {
    @Update("update course set number = number + 1 where id =#{id}")
    public void addCourseNumber(int courseId);
}