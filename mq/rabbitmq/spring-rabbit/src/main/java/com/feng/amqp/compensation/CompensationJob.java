package com.feng.amqp.compensation;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 补偿机制：定时任务将MQ失败的消息重新交给MemberService
 * @Author fengsy
 * @Date 10/29/21
 */
@Component
@Slf4j
public class CompensationJob {

    private static ThreadPoolExecutor compensationThreadPool = new ThreadPoolExecutor(
            10, 10,
            1, TimeUnit.HOURS,
            new ArrayBlockingQueue<>(1000),
            new ThreadFactoryBuilder().setNameFormat("compensation-threadpool-%d").build());
    @Resource
    private UserService userService;
    @Resource
    private MemberService memberService;
    private long offset = 0;

    @Scheduled(initialDelay = 10_000, fixedRate = 5_000)
    public void compensationJob() {
        log.info("开始从用户ID {} 补偿", offset);
        userService.getUsersAfterIdWithLimit(offset, 5).forEach(user -> {
            compensationThreadPool.execute(() -> memberService.send(user));
            offset = user.getId();
        });
    }

}
