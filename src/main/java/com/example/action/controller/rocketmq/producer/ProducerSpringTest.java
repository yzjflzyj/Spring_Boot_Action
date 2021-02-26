package com.example.action.controller.rocketmq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class ProducerSpringTest {

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    //发送普通消息的示例
    @RequestMapping("/test_producer")
    public void sendMessage(String topic,String msg){
        this.rocketMQTemplate.convertAndSend(topic,msg);
    }
    //发送事务消息的示例
    @RequestMapping("/test_transaction")
    public void sendMessageInTransaction(String topic,String msg) throws InterruptedException {
        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            Message<String> message = MessageBuilder.withPayload(msg).build();
            String destination =topic+":"+tags[i % tags.length];
            SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(destination, message,destination);
            log.info("%s%n", sendResult);

            Thread.sleep(10);
        }
    }
}
