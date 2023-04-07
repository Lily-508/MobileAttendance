package com.as.attendance_springboot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 对Redis操作封装
 * @author xulili
 */
@Component
public final class RedisUtil {
    @Autowired
    private  RedisTemplate redisTemplate;

    /**
     * 写入缓存并设置过期时间
     * @param key
     * @param value
     * @param timeout <=0设置为无限期限
     */
    public  void set(String key, Object value, long timeout) {
        System.out.println("Redis start");
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        if (timeout > 0) {
            operations.set(key, value, timeout, TimeUnit.SECONDS);
            System.out.println("Redis");
        }
    }

    /**
     * 读取缓存数据
     * @param key
     * @return
     */
    public  Object get(final String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置缓存时间
     * @param key
     * @param timeout 秒
     * @return
     */
    public  boolean expire(String key, long timeout) {
        if (timeout > 0) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    /**
     * 获取过期时间
     * @param key
     * @return
     */
    public  long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存咋
     * @param key
     * @return
     */
    public  boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
    public boolean remove(String key){
        if(hasKey(key)){
            return redisTemplate.delete(key);
        }else {
            return false;
        }
    }
}
