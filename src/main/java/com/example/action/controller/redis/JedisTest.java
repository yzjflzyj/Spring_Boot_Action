package com.example.action.controller.redis;

import redis.clients.jedis.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JedisTest {
    public static void main(String[] args) throws IOException {

        cluster();

        single();

        sentinel();
    }

    /**
     * @Author 严志杰
     * @Description 哨兵模式
     * @Date 0:06 2021/7/13
     * @Param []
     * @return void
     **/
    private static void sentinel() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(5);

        String masterName = "master_2";
        Set<String> sentinels = new HashSet<String>();
        sentinels.add(new HostAndPort("192.168.218.133",26379).toString());
        sentinels.add(new HostAndPort("192.168.218.133",26380).toString());
        sentinels.add(new HostAndPort("192.168.218.133",26381).toString());
        //JedisSentinelPool其实本质跟JedisPool类似，都是与redis主节点建立的连接池
        //JedisSentinelPool并不是说与sentinel建立的连接池，而是通过sentinel发现redis主节点并与其建立连接
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, config, 3000, null);
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            System.out.println(jedis.set("sentinel", "刘备"));
            System.out.println(jedis.get("sentinel"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //注意这里不是关闭连接，在JedisPool模式下，Jedis会被归还给资源池。
            if (jedis != null)
                jedis.close();
        }
    }

    /***
     * @Author codeHu
     * @Description 集群模式
     * @Date 0:03 2021/7/13
     * @Param []
     * @return void
     **/
    private static void cluster() throws IOException {
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

        JedisCluster jedisCluster = null;
        try {
            //connectionTimeout：指的是连接一个url的连接等待时间
            //soTimeout：指的是连接上一个url，获取response的返回等待时间
            jedisCluster = new JedisCluster(jedisClusterNode, 6000, 5000, 10, null, config);
            System.out.println(jedisCluster.set("cluster", "马超"));
            System.out.println(jedisCluster.get("cluster"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedisCluster != null)
                jedisCluster.close();
        }
    }

    /**
     * @Author 严志杰
     * @Description //单机模式
     * @Date 0:03 2021/7/13
     * @Param []
     * @return void
     **/
    private static void single() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);

        // timeout，这里既是连接超时又是读写超时，从Jedis 2.8开始有区分connectionTimeout和soTimeout的构造函数
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "192.168.218.133", 6379, 3000, "root");
        //JedisPool jedisPool1 = new JedisPool(jedisPoolConfig, "192.168.218.133", 6380, 3000, null);
        //JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 3000, null);

        Jedis jedis = null;
        //Jedis jedis1 = null;
        try {
            //从redis连接池里拿出一个连接执行命3
            jedis = jedisPool.getResource();
            //jedis1 = jedisPool1.getResource();
            System.out.println(jedis.set("二弟", "关羽"));
            //System.out.println(jedis1.get("二弟"));

            //管道示例
            //管道的命令执行方式：cat redis.txt | redis-cli -h 127.0.0.1 -a password - p 6379 --pipe
            /*Pipeline pl = jedis.pipelined();
            for (int i = 0; i < 10; i++) {
                pl.incr("pipelineKey");
                pl.set("zhuge" + i, "zhuge");
            }
            List<Object> results = pl.syncAndReturnAll();
            System.out.println(results);*/

            //lua脚本模拟一个商品减库存的原子操作
            //lua脚本命令执行方式：redis-cli --eval /tmp/test.lua , 10
            /*jedis.set("product_count_10016", "15");  //初始化商品10016的库存
            String script = " local count = redis.call('get', KEYS[1]) " +
                            " local a = tonumber(count) " +
                            " local b = tonumber(ARGV[1]) " +
                            " if a >= b then " +
                            "   redis.call('set', KEYS[1], a-b) " +
                            "   return 1 " +
                            " end " +
                            " return 0 ";
            Object obj = jedis.eval(script, Arrays.asList("product_count_10016"), Arrays.asList("10"));
            System.out.println(obj);*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //注意这里不是关闭连接，在JedisPool模式下，Jedis会被归还给资源池。
            if (jedis != null)
                jedis.close();
        }
    }
}
