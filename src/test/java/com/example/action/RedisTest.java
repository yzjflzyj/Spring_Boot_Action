package com.example.action;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

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
}
