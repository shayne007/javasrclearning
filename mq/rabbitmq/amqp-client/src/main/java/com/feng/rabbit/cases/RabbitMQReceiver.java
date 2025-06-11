package com.feng.rabbit.cases;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 10/31/21
 */

@Slf4j
public class RabbitMQReceiver {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("106.15.66.38");
        factory.setUsername("root");
        factory.setPassword("root");
        factory.setPort(5672);

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        channel.basicQos(1, true);

        boolean autoAck = false;
        channel.basicConsume("order-summary-queue", autoAck,
                new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties properties,
                                               byte[] body) throws IOException {
                        long deliveryTag = envelope.getDeliveryTag();

                        //用Random来模拟有时处理成功有时处理失败的场景
                        if (new Random().nextBoolean()) {
                            log.info("成功消费消息" + deliveryTag);
                            channel.basicAck(deliveryTag, false);
                        } else {
                            if (!envelope.isRedeliver()) {
                                log.warn("首次消费消息" + deliveryTag + "不成功，尝试重试");
                                channel.basicNack(deliveryTag, false, true);
                            } else {
                                log.warn("第二次消费消息" + deliveryTag + "不成功，扔到DLX");
                                channel.basicNack(deliveryTag, false, false);
                            }
                        }
                    }
                });
    }
}

