package com.feng;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */

@RestController
public class GcTestController {

    private Queue<Greeting> objCache = new ConcurrentLinkedDeque<>();

    @RequestMapping("/greeting")
    public Greeting greeting() {
        Greeting greeting = new Greeting("Hello World!");

        if (objCache.size() >= 200000) {
            objCache.clear();
        } else {
            objCache.add(greeting);
        }
        return greeting;
    }
}

@Data
@AllArgsConstructor
class Greeting {
    private String message;
}