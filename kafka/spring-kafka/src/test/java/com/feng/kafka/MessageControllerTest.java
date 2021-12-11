package com.feng.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/1/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessageControllerTest.class)
@EmbeddedKafka(count = 2, ports = {9092, 9093}, brokerPropertiesLocation = "classpath:application.yml")
public class MessageControllerTest {
    @Autowired
    private KafkaProperties properties;

    @Test
    public void testCreateToipc() {
        AdminClient client = AdminClient.create(properties.buildAdminProperties());
        if (client != null) {
            try {
                Collection<NewTopic> newTopics = new ArrayList<>(1);
                newTopics.add(new NewTopic("topic-kl", 1, (short) 1));
                client.createTopics(newTopics);
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                client.close();
            }
        }
    }
}
