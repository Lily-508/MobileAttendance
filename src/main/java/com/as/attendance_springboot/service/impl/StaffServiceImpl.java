package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.StaffMapper;
import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.service.StaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 */
@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {
}
