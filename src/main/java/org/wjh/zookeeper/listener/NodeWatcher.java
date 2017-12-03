package org.wjh.zookeeper.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.stereotype.Component;

@Component
public class NodeWatcher {
    public void watch(CuratorFramework client, String path, final NodeEventInvoker invoker) throws Exception {
        @SuppressWarnings("resource")
        PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {

            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                // TODO Auto-generated method stub
                invoker.invode(event);
            }
        });
    }
}
