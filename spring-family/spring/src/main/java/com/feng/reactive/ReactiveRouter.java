package com.feng.reactive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author fengsy
 * @date 8/5/21
 * @Description
 */
@Configuration
public class ReactiveRouter {
    @Bean
    public RouterFunction<ServerResponse> route(ReactiveHandler handler) {

        return RouterFunctions
            .route(RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), handler::hello);
    }
}
