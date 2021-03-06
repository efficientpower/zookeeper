package org.wjh.zookeeper.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.wjh.zookeeper.common.JsonUtils;
import org.wjh.zookeeper.common.ZookeeperUtils;

public class ZkNodeListener {
    @Autowired
    private NodeWatcher nodeWatcher;

    private String node;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void init() {
        try {
            NodeEventInvoker invoker = new NodeEventInvoker(){
                @Override
                public void invoke(PathChildrenCacheEvent event) {
                    // TODO Auto-generated method stub
                    ChildData data = event.getData();
                    System.out.println(JsonUtils.toString(data)+"==="+new String(data.getData()));
                }
            };
            CuratorFramework client = ZookeeperUtils.getZookeeper();
            String path = "/" + node;
            nodeWatcher.watch(client, path, invoker);
        } catch (Exception e) {
            
        }
    }
}
