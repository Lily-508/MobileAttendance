package com.as.attendance_springboot.service;

import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.model.enum_model.StaffRight;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StaffService extends IService<Staff> {
    int saveStaff(int dId, String sName, String pwd, StaffRight staffRight);
}
