package com.example.action.controller.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.nodes.GroupMember;
import org.junit.jupiter.api.Test;

@Slf4j
public class PathCacheTest extends CuratorClientTest{

    public static final String PATH="/path-cache";

    @Test
    public void testPathCache() throws Exception {

        createIfNeed(PATH);
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorClient, PATH, true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                log.info("监听event:  {}",event);
            }
        });

        // 如果设置为true则在首次启动时就会缓存节点内容到Cache中
        pathChildrenCache.start(true);

        curatorClient.setData().forPath(PATH+"/child", "changed!".getBytes());
        byte[] bytes = curatorClient.getData().forPath(PATH);
        log.info("get data from  node /node-cache :{}  successfully.", new String(bytes));
    }
}
