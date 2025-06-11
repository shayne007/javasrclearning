package com.feng.rabbit.config;

import lombok.Data;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 10/30/21
 */
@Data
public class MqConfigProperties {
    private String consumer;
    private String host = "106.15.66.38";
    private String port = "5672";
    private String user = "root";
    private String password = "root";
}
