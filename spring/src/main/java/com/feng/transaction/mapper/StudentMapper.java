package com.feng.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

@Mapper
public interface StudentMapper extends BaseMapper {
    @Insert("INSERT INTO `student`(`realname`) VALUES (#{realname})")
    void saveStudent(String realname);
}
