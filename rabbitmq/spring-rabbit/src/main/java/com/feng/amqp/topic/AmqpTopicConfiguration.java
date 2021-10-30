package com.feng.amqp.topic;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 10/28/21
 */

@Profile({"tut5", "topics"})
@Configuration
public class AmqpTopicConfiguration {
    @Bean
    public TopicExchange topic() {
        return new TopicExchange("tut.topic");
    }

    @Profile("receiver")
    private static class ReceiverConfig {

        @Bean
        public TopicReceiver receiver() {
            return new TopicReceiver();
        }

        @Bean
        public Queue autoDeleteQueue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue2() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1a(TopicExchange exchange, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(exchange).with("*.orange.*");
        }

        @Bean
        public Binding binding1b(TopicExchange exchange, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(exchange).with("*.*.rabbit");
        }

        @Bean
        public Binding binding2a(TopicExchange exchange, Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2).to(exchange).with("lazy.#");
        }

    }

    @Profile("sender")
    @Bean
    public TopicSender sender() {
        return new TopicSender();
    }
}
