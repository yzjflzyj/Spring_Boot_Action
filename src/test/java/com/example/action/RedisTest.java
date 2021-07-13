package com.example.action;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void testSelect() {

        redisTemplate.opsForValue().set("key","value");
        stringRedisTemplate.opsForValue().set("啊哈", "哈");
        redisTemplate.opsForList().leftPush("list","node1");
        redisTemplate.opsForList().leftPush("list","node2");
        redisTemplate.opsForList().leftPush("list","node3");
        System.out.println(redisTemplate.opsForList().rightPop("list"));
        System.out.println(redisTemplate.opsForList().rightPop("list"));
        System.out.println(redisTemplate.opsForList().rightPop("list"));
    }

    @Test
    public void setSelect() {
        stringRedisTemplate.opsForValue().set("测试1", "值1",10, TimeUnit.SECONDS);
        Object andSet1 = stringRedisTemplate.opsForValue().getAndSet("测试1", "value");
        stringRedisTemplate.opsForValue().set("测试2", "值2",30, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().setIfAbsent("inCreate", "0", 10, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().increment("inCreate");
    }
}
