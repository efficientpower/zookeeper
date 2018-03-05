package org.wjh.zookeeper.common;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;

public class ZookeeperLockContext implements InitializingBean {

    public static final String LOCK_ROOT = "/locks";
    private String namespace;
    private String bizStage;
    private int concurrency;
    private CuratorFramework client;
    private HashCalculator hashCalculator = new HashCalculator(concurrency);

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getBizStage() {
        return bizStage;
    }

    public void setBizStage(String bizStage) {
        this.bizStage = bizStage;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    @SuppressWarnings("serial")
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            init();
        } catch (Exception e) {
            throw new BeansException("init zookeeper context error", e) {};
        }
    }

    /**
     * 锁路径初始化
     * 
     * @throws Exception
     */
    public void init() throws Exception {
        if (concurrency < 0 || StringUtils.isBlank(namespace) || StringUtils.isBlank(bizStage)) {
            String errorInfo = String.format("illegal arguments for init. concurrency=%d;namespace=%s;bizStage=%s ", concurrency, namespace, bizStage);
            throw new Exception(errorInfo);
        }
        hashCalculator = new HashCalculator(concurrency);
        client = ZookeeperUtils.getZookeeper();
        try {
            String path = getLockPathStaticPart().toString();
            Stat stat = client.checkExists().forPath(path);
            if (stat == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, new byte[0]);
            }
        } catch (Exception e) {
            throw new Exception("can not init ZookeeperLockContext!", e);
        }
    }

    /**
     * 锁永久路径
     * 
     * @return
     */
    private StringBuilder getLockPathStaticPart() {
        StringBuilder sb = new StringBuilder();
        sb.append("/").append(getNamespace()).append(LOCK_ROOT).append("/").append(bizStage);
        return sb;
    }

    /**
     * 锁临时路径
     * 
     * @param key
     * @return
     */
    private StringBuilder getLockPathWithAll(String key) {
        if (StringUtils.isBlank(key)) {
            key = "";
        }
        int keyHash = hashCalculator.getIndexOfValue(key.hashCode());
        return getLockPathStaticPart().append("/").append(keyHash);
    }

    /**
     * 获取锁上下文
     * 
     * @param key
     * @return
     */
    public ZookeeperLock getLock(String key) {
        try {
            String canonicPath = getLockPathWithAll(key).toString();
            InterProcessMutex lock = new InterProcessMutex(client, canonicPath);
            return new ZookeeperLock(lock, canonicPath);
        } catch (Exception e) {
            throw new RuntimeException("can not getLock!", e);
        }
    }

}
