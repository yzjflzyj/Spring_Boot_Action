package com.example.action.controller.redis;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonBloomFilter {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        //构造Redisson
        RedissonClient redisson = Redisson.create(config);

        RBloomFilter<String> bloomFilter = redisson.getBloomFilter("nameList");
        //初始化布隆过滤器：预计元素为100000000L,误差率为3%,根据这两个参数会计算出底层的bit数组大小
        bloomFilter.tryInit(100000000L,0.03);
        //将zhuge插入到布隆过滤器中
        bloomFilter.add("刘备");

        //判断下面号码是否在布隆过滤器中
        System.out.println(bloomFilter.contains("关羽"));//false
        System.out.println(bloomFilter.contains("张飞"));//false
        System.out.println(bloomFilter.contains("刘备"));//true
    }

    //把所有数据存入布隆过滤器
    //伪代码:初始化(布隆过滤器不能删除,只能重新初始化)
//    void init(){
//        for (String key: keys) {
//            bloomFilter.put(key);
//        }
//    }
//  伪代码
//    String get(String key) {
//        // 从布隆过滤器这一级缓存判断下key是否存在
//        Boolean exist = bloomFilter.contains(key);
//        if(!exist){
//            return "";
//        }
//        // 从缓存中获取数据
//        String cacheValue = cache.get(key);
//        // 缓存为空
//        if (StringUtils.isBlank(cacheValue)) {
//            // 从存储中获取
//            String storageValue = storage.get(key);
//            cache.set(key, storageValue);
//            // 如果存储数据为空， 需要设置一个过期时间(300秒)
//            if (storageValue == null) {
//                cache.expire(key, 60 * 5);
//            }
//            return storageValue;
//        } else {
//            // 缓存非空
//            return cacheValue;
//        }
//    }
}
