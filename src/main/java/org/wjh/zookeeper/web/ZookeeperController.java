package org.wjh.zookeeper.web;

import javax.annotation.Resource;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wjh.zookeeper.common.IdUtils;
import org.wjh.zookeeper.common.ZookeeperLock;
import org.wjh.zookeeper.common.ZookeeperLockContext;
import org.wjh.zookeeper.common.ZookeeperUtils;

@Controller
public class ZookeeperController {

    @Resource(name="helloLockContext")
    private ZookeeperLockContext helloLockContext;

    @RequestMapping("/zk/hello.do")
    @ResponseBody
    public Object hello(@RequestParam(value="data", required=false, defaultValue="hello") String data) {
        ZookeeperLock lock = null;
        try{
//            lock = helloLockContext.getLock("hello");
//            lock.lock();
//            String path = "/hello-dev/task/1";
//            CuratorFramework client = ZookeeperUtils.getZookeeper();
//            if(client.checkExists().forPath(path) == null){
//                client.create().forPath(path, data.getBytes());
//            }else{
//                client.setData().forPath(path, data.getBytes());
//            }
            return IdUtils.getCode("HZ", 6);
        }catch(Exception e){
            return "ERROR";
        }finally {
            if(lock != null){
               lock.unLock();
            }
        }
    }
}
