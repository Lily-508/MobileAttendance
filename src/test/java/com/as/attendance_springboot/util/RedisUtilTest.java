package com.as.attendance_springboot.util;

import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

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
    @Autowired
    private StaffServiceImpl staffService;
    @Test
    void test(){
        LambdaQueryWrapper<Staff> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Staff::getDId,101);
        IPage<Staff> page=staffService.page(new Page(1,2),lambdaQueryWrapper);
        System.out.println(page.getRecords());

    }
}
