package com.example.action.controller.rocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "MyConsumerGroup", topic = "TestTopic",consumeMode= ConsumeMode.CONCURRENTLY)
public class SpringConsumerTest implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("Received message : "+ message);
    }

}
