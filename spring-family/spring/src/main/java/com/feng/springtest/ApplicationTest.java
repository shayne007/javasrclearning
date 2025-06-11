package com.feng.springtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * @author fengsy
 * @date 7/13/21
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class ApplicationTest {

    @Autowired
    public HelloController helloController;

    @Test
    public void testController() throws Exception {
        String response = helloController.hi();
        Assert.notNull(response, "not null");
    }

}