package org.wjh.zookeeper.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.curator.framework.CuratorFramework;

public class IdUtils {

    public static final String SERIAL_ROOT="/serialId";
    /**
     * 根据某个结点的修改版本号获取全局有序序号（0-2^31-1）
     * 
     * @param prefix
     * @return
     */
    public static String getId(String prefix) {
        String path = SERIAL_ROOT +"/" + prefix;
        int count = 0;
        try {
            CuratorFramework client = ZookeeperUtils.getZookeeper();
            if (client.checkExists().forPath(path) == null) {
                client.create().creatingParentContainersIfNeeded().forPath(path, new byte[0]);
            }
            int id = client.setData().forPath(path, new byte[0]).getVersion();
            return String.valueOf(id);
        } catch (Exception e) {
            count++;
            if (count > 2) {
                throw new RuntimeException("get id failed");
            }
            return getId(prefix);
        }
    }

    /**
     * 获取制定长度的有前缀的有序编码
     * 
     * @param prefix
     * @param length
     * @return
     */
    public static String getCode(String prefix, int length) {
        String monthPath = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        String id = getId(prefix + "/" + monthPath);
        if(id.length() > length) {
            throw new RuntimeException("超过id最长限制");
        }
        while(id.length() < length) {
            id = "0" + id;
        }
        String code = prefix + monthPath + id;
        System.out.println(code);
        return code;
    }
}
