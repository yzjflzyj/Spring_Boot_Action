package com.example.action.controller.redis;


import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisConfig {
    /*redisTemplate,但是一般还是用stringRedisTemplate*/
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //GenericJackson2JsonRedisSerializer反序列化时有问题,GenericFastJsonRedisSerializer有安全漏洞,但在此还是用fastjson
        redisTemplate.setValueSerializer(new GenericFastJsonRedisSerializer());

        return redisTemplate;
    }

    /*单机默认*/
    @Bean
    public Jedis jedisSingle(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);

        // timeout，这里既是连接超时又是读写超时，从Jedis 2.8开始有区分connectionTimeout和soTimeout的构造函数
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "192.168.218.133", 6379, 3000, "root");
        return jedisPool.getResource();
    }

    /*集群模式*/
    @Bean
    public JedisCluster jedisCluster(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(5);

        Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort("192.168.218.131", 8001));
        jedisClusterNode.add(new HostAndPort("192.168.218.132", 8002));
        jedisClusterNode.add(new HostAndPort("192.168.218.133", 8003));
        jedisClusterNode.add(new HostAndPort("192.168.218.131", 8004));
        jedisClusterNode.add(new HostAndPort("192.168.218.132", 8007));
        jedisClusterNode.add(new HostAndPort("192.168.218.133", 8006));
        return new JedisCluster(jedisClusterNode, 6000, 5000, 10, null, config);
    }
}
