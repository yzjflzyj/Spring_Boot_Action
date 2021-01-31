package com.example.action.controller.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ZookeeperClientTest {

    private static final String ZK_ADDRESS="192.168.218.129:2181";

    private static final int SESSION_TIMEOUT = 5000;

    private static ZooKeeper zooKeeper;

    private static final String ZK_NODE="/zk-node";


    @BeforeEach
    public void init() throws IOException, InterruptedException {
        final CountDownLatch countDownLatch=new CountDownLatch(1);
        zooKeeper=new ZooKeeper(ZK_ADDRESS, SESSION_TIMEOUT, event -> {
            if (event.getState()== Watcher.Event.KeeperState.SyncConnected &&
                    event.getType()== Watcher.Event.EventType.None){
                countDownLatch.countDown();
                log.info("连接成功！");
            }
        });
        log.info("连接中....");
        countDownLatch.await();
    }

    /**
     * 同步创建节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void createTest() throws KeeperException, InterruptedException, IOException {
        String path = zooKeeper.create(ZK_NODE, "data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        log.info("created path: {}",path);
    }




    /**
     * 异步创建节点
     * @throws InterruptedException
     */
    @Test
    public void createAsycTest() throws InterruptedException {
        zooKeeper.create(ZK_NODE, "data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                (rc, path, ctx, name) -> log.info("rc  {},path {},ctx {},name {}",rc,path,ctx,name),"context");
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }


    /**
     * 修改节点数据
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void setTest() throws KeeperException, InterruptedException {

        Stat stat = new Stat();
        //获取节点数据
        byte[] data = zooKeeper.getData(ZK_NODE, false, stat);
        log.info("修改前: {}",new String(data));
        zooKeeper.setData(ZK_NODE, "changed!".getBytes(), stat.getVersion());
        byte[] dataAfter = zooKeeper.getData(ZK_NODE, false, stat);
        log.info("修改后: {}",new String(dataAfter));
    }
}
