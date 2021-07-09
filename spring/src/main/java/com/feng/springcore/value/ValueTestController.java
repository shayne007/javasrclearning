package com.feng.springcore.value;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Value注解可用于读取配置属性、环境变量、系统参数； 处理读取String类型，还可以读取对象及其属性
 * 
 * @author fengsy
 * @date 7/8/21
 * @Description
 */
@RestController
@Slf4j
public class ValueTestController {
    @Value("#{xie}")
    private User user;

    @Value("#{xie.name}")
    private String name;

    @Value("${home}")
    private String home;

    /**
     * 读取配置文件中的信息注意不要与系统参数、环境变量名称冲突
     */
    @Value("${user}")
    private String usernameSys;
    @Value("${user.name}")
    private String userNameParam;
    @Value("${username}")
    private String usernameProp;
    @Value("${password}")
    private String password;

    @RequestMapping(path = "user", method = RequestMethod.GET)
    public String getUser() {
        log.info(user.getName());
        log.info(name);
        log.info(home);
        log.info(usernameSys);
        log.info(userNameParam);
        log.info(usernameProp);
        return usernameProp + ":" + password;
    }

    @Bean(name = "xie")
    public User user() {
        User user = new User(1, "xie");
        return user;
    }

    private class User {
        private String name;

        public User(long i, String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}