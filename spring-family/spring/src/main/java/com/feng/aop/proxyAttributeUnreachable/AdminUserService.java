package com.feng.aop.proxyAttributeUnreachable;

import org.springframework.stereotype.Service;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */
@Service
public class AdminUserService {
    public final User adminUser = new User("202101166");

    public void login() {
        System.out.println("admin user login...");
    }

    public User getUser() {
        return adminUser;
    }
}