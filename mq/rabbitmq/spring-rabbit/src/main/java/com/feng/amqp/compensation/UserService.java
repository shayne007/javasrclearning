package com.feng.amqp.compensation;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Description User service 模拟用户注册，保存至数据库
 * @Author fengsy
 * @Date 10/29/21
 */
@Service
public class UserService {
    private List<User> users = new ArrayList<>();

    public User register() {
        User user = new User();
        users.add(user);
        return user;
    }

    public List<User> getUsersAfterIdWithLimit(long id, int limit) {
        return users.stream()
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) {
                        return user.getId() >= id;
                    }
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
}
