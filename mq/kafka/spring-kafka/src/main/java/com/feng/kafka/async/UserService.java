package com.feng.kafka.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/12/21
 */
@Service
public class UserService {

    @Async("asyncExecutor")
    public void handleData(UserDTO userDTO, CountDownLatch countDownLatch) {
        try {
            //模拟耗时的IO处理
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName() + " deal with: " + userDTO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }
}
