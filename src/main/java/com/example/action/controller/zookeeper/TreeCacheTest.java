package com.example.action.controller.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.junit.jupiter.api.Test;

@Slf4j
public class TreeCacheTest extends CuratorClientTest{

    public static final String TREE_CACHE="/tree-path";

    @Test
    public void testTreeCache() throws Exception {
        createIfNeed(TREE_CACHE);
        TreeCache treeCache = new TreeCache(curatorClient, TREE_CACHE);
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                log.info(" tree cache: {}",event);
            }
        });
        treeCache.start();

        curatorClient.setData().forPath(TREE_CACHE, "changed!".getBytes());
        byte[] bytes = curatorClient.getData().forPath(TREE_CACHE);
        log.info("get data from  node /node-cache :{}  successfully.", new String(bytes));
    }
}
