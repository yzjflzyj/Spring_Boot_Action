package com.example.action.controller.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CuratorClientTest {

    protected static CuratorFramework curatorClient;
    protected static final String ZK_ADDRESS="192.168.218.131:2181";
    protected static final int SESSION_TIMEOUT = 50000;// 会话超时时间
    protected static final int CONNECTION_TIMEOUT = 50000;// 连接超时时间

    @BeforeEach
    public void init(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        curatorClient = CuratorFrameworkFactory.builder()
                .connectString(ZK_ADDRESS)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .connectionTimeoutMs(CONNECTION_TIMEOUT)
                .retryPolicy(retryPolicy)
                .namespace("base") // 包含隔离名称
                .build();
        curatorClient.start();
    }

    /**
     * 创建节点(可用withMode指定创建节点的类型, 默认是持久型)
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        String path = curatorClient.create().forPath("/curator-node");
        // curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/curator-node","some-data".getBytes())
        log.info("curator create node :{}  successfully.",path);
    }

    /**
     * 一次性创建带层级结构的节点
     * @throws Exception
     */
    @Test
    public void testCreateWithParent() throws Exception {
        String pathWithParent="/node-parent/sub-node-1";
        String path = curatorClient.create().creatingParentsIfNeeded().forPath(pathWithParent);
        log.info("curator create node :{}  successfully.",path);
    }

    /**
     * 获取节点数据
     * @throws Exception
     */
    @Test
    public void testGetData() throws Exception {
        byte[] bytes = curatorClient.getData().forPath("/curator-node");
        log.info("get data from  node :{}  successfully.",new String(bytes));
    }

    /**
     * 修改节点数据
     * @throws Exception
     */
    @Test
    public void testSetData() throws Exception {
        curatorClient.setData().forPath("/curator-node","changed!".getBytes());
        byte[] bytes = curatorClient.getData().forPath("/curator-node");
        log.info("get data from  node /curator-node :{}  successfully.",new String(bytes));
    }

    /**
     * 删除节点数据
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        String pathWithParent="/node-parent";
        curatorClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(pathWithParent);
    }


    /**
     * 异步获取节点数据(默认在 EventThread 中调用)
     * @throws Exception
     */
    @Test
    public void testAsyc() throws Exception {
        curatorClient.getData().inBackground((item1, item2) -> {
            log.info(" background: {}", item2);
        }).forPath("/curator-node");

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    /**
     * 异步获取节点数据(指定线程池)
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        curatorClient.getData().inBackground((item1, item2) -> {
            log.info(" background: {}", item2);
        },executorService).forPath("/curator-node");

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    public void createIfNeed(String NODE_CACHE){
        try {
            if (curatorClient.checkExists().forPath(NODE_CACHE) == null) {
                String path = curatorClient.create().forPath(NODE_CACHE);
                log.info("curator create node :{}  successfully.", path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
