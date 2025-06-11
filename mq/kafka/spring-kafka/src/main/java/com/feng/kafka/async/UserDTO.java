package com.feng.kafka.async;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/12/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name = "xiaoming";
    private int age = 21;

}
