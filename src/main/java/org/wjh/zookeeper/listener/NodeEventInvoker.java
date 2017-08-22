package org.wjh.zookeeper.listener;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

public interface NodeEventInvoker {
    public void invode(PathChildrenCacheEvent event);
}
