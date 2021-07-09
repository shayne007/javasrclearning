package com.feng.springcore.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.Ordered;

/**
 * @author fengsy
 * @date 7/8/21
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Ordered {
    private int id;
    private String name;

    @Override
    public int getOrder() {
        return id;
    }
}
