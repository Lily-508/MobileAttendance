package com.as.attendance_springboot.service;

import com.as.attendance_springboot.model.Staff;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StaffServiceTest {
    @Autowired
    private StaffService staffService;
    @Test
    void getStaffByPage(){
        QueryWrapper<Staff>queryWrap=new QueryWrapper<>();
        queryWrap.eq("s_status",0);
        IPage<Staff> page=staffService.page(new Page(1,2),queryWrap);
        System.out.println(page.getRecords());

    }
}
