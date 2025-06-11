package com.feng.springtest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author fengsy
 * @date 7/13/21
 * @Description
 */
@Configuration
@ImportResource(locations = {"classpath:/spring.xml"})
public class Config {}