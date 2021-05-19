package com.feng.concurrency.patterns.tspecstorage;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */

@Data
@AllArgsConstructor
public class Stu {
    private String name;
    private int age;
}