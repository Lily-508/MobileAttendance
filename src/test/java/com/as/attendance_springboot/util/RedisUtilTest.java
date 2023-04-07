package com.as.attendance_springboot.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description redis test
 * @date 2023/4/7 19:54:51
 */
@SpringBootTest
public class RedisUtilTest {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Test
    void test(){
        System.out.println("Redis start");
        ValueOperations<String,String> operations = redisTemplate.opsForValue();
        operations.set("key","123",60, TimeUnit.SECONDS);
        System.out.println("Redis");

    }
}
