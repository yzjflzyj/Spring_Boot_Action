package com.example.action.controller.redis;
import org.apache.commons.lang.StringUtils;

import java.util.Random;

/**
 * redis调优
 */
public class RedisTuning {

    /**
     * 缓存失效
     * @param key
     * @return
     */
//    String get(String key) {
//        // 从缓存中获取数据
//        String cacheValue = cache.get(key);
//        // 缓存为空
//        if (StringUtils.isBlank(cacheValue)) {
//            // 从存储中获取
//            String storageValue = storage.get(key);
//            cache.set(key, storageValue);
//            //设置一个过期时间(300到600之间的一个随机数)
//            int expireTime = new Random().nextInt(300)  + 300;
//            if (storageValue == null) {
//                cache.expire(key, expireTime);
//            }
//            return storageValue;
//        } else {
//            // 缓存非空
//            return cacheValue;
//        }
//    }

    /**
     * 用互斥锁来解决大量线程同时重建缓存
     * @param key
     * @return
     */
//    String get(String key) {
//        // 从Redis中获取数据
//        String value = redis.get(key);
//        // 如果value为空， 则开始重构缓存
//        if (value == null) {
//            // 只允许一个线程重建缓存， 使用nx， 并设置过期时间ex
//            String mutexKey = "mutext:key:" + key;
//            if (redis.set(mutexKey, "1", "ex 180", "nx")) {
//                // 从数据源获取数据
//                value = db.get(key);
//                // 回写Redis， 并设置过期时间
//                redis.setex(key, timeout, value);
//                // 删除key_mutex
//                redis.delete(mutexKey);
//            }// 其他线程休息50毫秒后重试
//            else {
//                Thread.sleep(50);
//                get(key);
//            }
//        }
//        return value;
//    }



    
}
