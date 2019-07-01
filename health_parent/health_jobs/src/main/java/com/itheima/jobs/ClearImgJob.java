package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * 定时任务-清理垃圾图片
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 清理图片方法
     */
    public void clearImg() {
        //计算出两个set集合的差值  set1-set2
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iterator = sdiff.iterator();
        //遍历集合
        while (iterator.hasNext()) {
            String fileName = iterator.next();
            //清理七牛云上的图片
            QiniuUtils.deleteFileFromQiniu(fileName);
            // 清理set1集合中多余图片名称  bug?
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
        }

    }
}
