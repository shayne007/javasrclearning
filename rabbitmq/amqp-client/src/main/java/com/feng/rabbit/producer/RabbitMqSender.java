package com.feng.rabbit;

import com.feng.rabbit.config.MqConfigProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 10/30/21
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RabbitMqSender implements MqSender {
    private MqConfigProperties configProperties;

    private ConnectionFactory factory = null;// 连接工厂
    private Connection connection = null;
    private Channel channel = null;


    public RabbitMqSender(MqConfigProperties configProperties) {
        this.configProperties = configProperties;
        init();

    }

    @Override
    public void sendMessage(String body, String topic, String group) {

        try {
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)//Marks a message as persistent
                    .contentEncoding("UTF-8")
                    .contentType("text/plain")
                    .expiration("10000")
                    .build();
            channel.basicPublish(topic, group, properties, body.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化
     */
    private void init() {
        if (channel != null) {
            return;
        }

        try {
            factory = new ConnectionFactory();
            factory.setHost(configProperties.getHost());
            factory.setPort(Integer.parseInt(configProperties.getPort()));
            factory.setUsername(configProperties.getUser());
            factory.setPassword(configProperties.getPassword());
            // 获取连接
            connection = factory.newConnection();
            channel = connection.createChannel();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
