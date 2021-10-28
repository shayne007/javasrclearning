package com.feng.amqp;

import com.feng.amqp.rpc.RabbitAmqpRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description 应用启动类
 * @Author fengsy
 * @Date 10/28/21
 */
@SpringBootApplication
@EnableScheduling
public class RabbitAmqpApplication {
    @Profile("usage_message")
    @Bean
    public CommandLineRunner usage() {
        return args -> {
            System.out.println("This app uses Spring Profiles to control its behavior.\n");
            System.out.println("Options are: ");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=hello-world,receiver");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=hello-world,sender");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=work-queues,receiver");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=work-queues,sender");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=pub-sub,receiver");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=pub-sub,sender");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=routing,receiver");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=routing,sender");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=topics,receiver");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=topics,sender");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=rpc,client");
            System.out.println("java -jar rabbit-tutorials.jar --spring.profiles.active=rpc,server");
        };
    }

    @Profile("!usage_message")
    @Bean
    public CommandLineRunner tutorial() {
        return new RabbitAmqpRunner();
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitAmqpApplication.class, args);
    }
}
