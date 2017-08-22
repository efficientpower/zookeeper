package org.wjh.zookeeper.common;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * zookeeper工具,用于获取CuratorFramework，使用时需要在spring中配置
 * 
 * @author wangjihui
 *
 */
public class ZookeeperUtils implements BeanFactoryAware {

    private static CuratorFramework CLIENT;
    private static ZookeeperUtils INSTANCE;

    private String connectString;
    private int sessionTimeoutMs = 60000;
    private int connectionTimeoutMs = 60000;
    private int baseSleepTimeMs = 10000;
    private int maxRetries = 3;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        // TODO Auto-generated method stub
        INSTANCE = beanFactory.getBean("zookeeperUtils", ZookeeperUtils.class);
    }

    public static CuratorFramework getZookeeper() {
        if (CLIENT != null) {
            return CLIENT;
        }
        synchronized (ZookeeperUtils.class) {
            if (CLIENT != null) {
                return CLIENT;
            }
            CuratorFramework cf = CuratorFrameworkFactory.builder()
                    .connectString(INSTANCE.connectString)
                    .connectionTimeoutMs(INSTANCE.connectionTimeoutMs)
                    .sessionTimeoutMs(INSTANCE.sessionTimeoutMs)
                    .retryPolicy(new ExponentialBackoffRetry(INSTANCE.baseSleepTimeMs, INSTANCE.maxRetries))
                    .build();
            if (cf != null) {
                cf.start();
                CLIENT = cf;
            } else {
                throw new RuntimeException("create CuratorFramework failed, connectString=" + INSTANCE.connectString);
            }
            return CLIENT;
        }
    }

}
