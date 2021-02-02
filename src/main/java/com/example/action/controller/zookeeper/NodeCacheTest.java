package com.example.action.controller.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.junit.jupiter.api.Test;

@Slf4j
public class NodeCacheTest extends CuratorClientTest {

    public static final String NODE_CACHE = "/node-cache";

    @Test
    public void testNodeCacheTest() throws Exception {

        createIfNeed(NODE_CACHE);
        NodeCache nodeCache = new NodeCache(curatorClient, NODE_CACHE);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.info("{} path nodeChanged: ", NODE_CACHE);
                printNodeData();
            }
        });

        nodeCache.start();

        curatorClient.setData().forPath(NODE_CACHE, "changed!".getBytes());
        byte[] bytes = curatorClient.getData().forPath(NODE_CACHE);
        log.info("get data from  node /node-cache :{}  successfully.", new String(bytes));
    }


    public void printNodeData() throws Exception {
        byte[] bytes = curatorClient.getData().forPath(NODE_CACHE);
        log.info("data: {}", new String(bytes));
    }
}