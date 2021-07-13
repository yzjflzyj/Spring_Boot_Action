package com.example.action.controller.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@Slf4j
public class RedisControllerTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private JedisCluster jedisCluster;
    @Value("${server.port}")
    private String serverPort;

    ReentrantLock lock = new ReentrantLock();

    public static final int inventoryCount = 100;
    public static final String REDIS_LOCK = "redisLock";

    /**
     * @Author 严志杰
     * @Description 模拟减库存操作
     * @Date 22:30 2021/7/8
     * @Param []
     */
    @RequestMapping("/reduce/inventory")
    public String ReduceInventory() {
        //return methodOneNode();
        //return methodSetNx();
        return methodSetNxOptimizationForDelete();
    }


    /**
     * 单机版,多线程存在超卖,单线程不存在
     * 多线程的安全问题,可以通过加锁实现,synchronized和reentrantLock来实现
     *
     * @return
     */
    private String methodOneNode() {
        lock.lock();
        try {
            String result = stringRedisTemplate.opsForValue().get("good:001");
            int count = result == null ? 0 : Integer.parseInt(result);
            if (count > 0) {
                count--;
                stringRedisTemplate.opsForValue().set("good:001", String.valueOf(count));
                log.info(serverPort + "端口号库存减1,当前库存为" + count);
                return serverPort + "端口号库存减1,当前库存为" + count;
            } else {
                log.info(serverPort + "端口号库存减1,当前库存为" + count);
                return serverPort + "该产品库存已经为0,已经卖完了";
            }
        } finally {
            lock.unlock();
        }

    }


    /**
     * 使用redis分布式锁,setNx
     *
     * @return
     */
    private String methodSetNx() {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value);
        if (!flag) {
            log.info("没有抢到redis分布式锁");
            return "没有抢到redis分布式锁";
        }
        String result = stringRedisTemplate.opsForValue().get("good:001");
        int count = result == null ? 0 : Integer.parseInt(result);
        if (count > 0) {
            count--;
            stringRedisTemplate.opsForValue().set("good:001", String.valueOf(count));
        }
        stringRedisTemplate.delete(REDIS_LOCK);
        log.info(serverPort + "端口号库存减1,当前库存为" + count);
        return serverPort + "端口号库存减1,当前库存为" + count;
    }


    /**
     * 使用redis分布式锁,setNx优化
     * 1.防止后续程序异常,没有解锁,将解锁放在finally里,并对分布式锁加过期时间
     * 2.由于有过期时间,因此可能会有当前锁过期了,其他实例获取锁,但是被当前实例解锁,因此加上分布式锁的value判断,确定是同一把锁
     *
     * @return
     */
    private String methodSetNxOptimization() {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value, 10, TimeUnit.MINUTES);   //1.设置过期时间,expire,但和setNx没有必要分开,因此放在一起保持原子性
        if (!flag) {
            log.info("没有抢到redis分布式锁");
            return "没有抢到redis分布式锁";
        }
        int count;
        try {
            String result = stringRedisTemplate.opsForValue().get("good:001");
            count = result == null ? 0 : Integer.parseInt(result);
            if (count > 0) {
                count--;
                stringRedisTemplate.opsForValue().set("good:001", String.valueOf(count));
            }
        } finally {
            if (value.equals(stringRedisTemplate.opsForValue().get(REDIS_LOCK))) {  //2.防止误解锁
                stringRedisTemplate.delete(REDIS_LOCK);
            }
        }
        log.info(serverPort + "端口号库存减1,当前库存为" + count);
        return serverPort + "端口号库存减1,当前库存为" + count;

    }

    /**
     * 使用redis分布式锁,setNx优化
     * 解锁的原子性保证,解锁时可能if判断过后,还没有执行delete时,锁已经过期了,锁已经被别的实例持有,此时删除仍然有问题,有线程安全问题,要保证删除的原子性
     * 方式1:jedis的lua脚本(推荐)
     * 方式2:redis的事务
     *
     * @return
     */
    private String methodSetNxOptimizationForDelete() {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value, 10, TimeUnit.MINUTES);   //1.设置过期时间,expire,但和setNx没有必要分开,因此放在一起保持原子性
        if (!flag) {
            log.info("没有抢到redis分布式锁");
            return "没有抢到redis分布式锁";
        }
        int count;
        try {
            String result = stringRedisTemplate.opsForValue().get("good:001");
            count = result == null ? 0 : Integer.parseInt(result);
            if (count > 0) {
                count--;
                stringRedisTemplate.opsForValue().set("good:001", String.valueOf(count));
            }
        } finally {
            //方式一:lua脚本
            String script = " if redis.call('get',KEYS[1]) == ARGV[1]  " +
                    " then " +
                    " return redis.call('del', KEYS[1]) " +
                    " else " +
                    "   return 0 " +
                    " end ";
            try {
                Object obj = jedisCluster.eval(script, Arrays.asList(REDIS_LOCK), Arrays.asList(value));
                if ("1".equals(obj.toString())) {
                    System.out.println("删除锁成功");
                } else {
                    System.out.println("删除锁失败");
                }

            } finally {
                if (jedisCluster != null) {
                    try {
                        jedisCluster.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //方式2:事务
            /*while (true) {
                stringRedisTemplate.watch(REDIS_LOCK);  //乐观锁,在REDIS_LOCK锁被人动过,那么删除事务就会失败,
                if (value.equals(stringRedisTemplate.opsForValue().get(REDIS_LOCK))) {  //2.防止误解锁
                    stringRedisTemplate.setEnableTransactionSupport(true);
                    stringRedisTemplate.multi();
                    stringRedisTemplate.delete(REDIS_LOCK);
                    List<Object> list = stringRedisTemplate.exec();
                    if (list == null) {
                        continue;
                    }
                    stringRedisTemplate.unwatch();
                    break;
                }
            }*/
        }
        log.info(serverPort + "端口号库存减1,当前库存为" + count);
        return serverPort + "端口号库存减1,当前库存为" + count;
    }
}
