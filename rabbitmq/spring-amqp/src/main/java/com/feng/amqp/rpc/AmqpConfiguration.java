package com.feng.amqp.rpc;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 10/28/21
 */

@Profile({"tut6", "rpc"})
@Configuration
public class AmqpConfiguration {
    @Profile("client")
    private static class ClientConfig {

        @Bean
        public DirectExchange exchange() {
            return new DirectExchange("tut.rpc");
        }

        @Bean
        public RPCClient client() {
            return new RPCClient();
        }

    }

    @Profile("server")
    private static class ServerConfig {

        @Bean
        public Queue queue() {
            return new Queue("tut.rpc.requests");
        }

        @Bean
        public DirectExchange exchange() {
            return new DirectExchange("tut.rpc");
        }

        @Bean
        public Binding binding(DirectExchange exchange,
                               Queue queue) {
            return BindingBuilder.bind(queue)
                    .to(exchange)
                    .with("rpc");
        }

        @Bean
        public RPCServer server() {
            return new RPCServer();
        }

    }
}
