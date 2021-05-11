package com.feng;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */

@SpringBootApplication
@EnableScheduling
@Slf4j
public class Application {

    // 创建线程池
    /*  private ExecutorService excutorService =
        new ThreadPoolExecutor(4096, 4096, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
     */
    private int counter;

    // 以固定的速率向线程池中加入任务
    // @Scheduled(fixedRate = 10)
    // public void lockContention(){
    // IntStream.range(0,1000000).forEach(i -> excutorService.submit(this::incrementSync));
    // }

    private synchronized void incrementSync() {
        counter = (counter + 1) % 10000000;
    }

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

}