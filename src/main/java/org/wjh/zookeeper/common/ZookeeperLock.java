package org.wjh.zookeeper.common;

import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public class ZookeeperLock {
    private String path;
    private InterProcessMutex lock;

    public ZookeeperLock(InterProcessMutex lock, String path) {
        this.lock = lock;
        this.path = path;
    }

    /**
     * 获取锁
     * 
     * @return
     */
    public void lock() {
        try {
            lock.acquire();
        } catch (Exception e) {
            throw new RuntimeException("acquire lock falied, path=" + path, e);
        }
    }

    /**
     * 设置超时时间的获取锁接口.
     *
     * @param timeout
     *            超时时间, 单位为秒
     * @return 是否获取成功.
     */
    public boolean lock(int timeout) {
        try {
            return lock.acquire(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("acquire lock falied, path=" + path, e);
        }
    }

    /**
     * 释放锁
     */
    public void unLock() {
        try {
            lock.release();
        } catch (Exception e) {
            throw new RuntimeException("release lock falied, path=" + path, e);
        }
    }

}
