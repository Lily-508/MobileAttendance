package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.OffsetAttendanceMapper;
import com.as.attendance_springboot.model.OffsetAttendance;
import com.as.attendance_springboot.service.OffsetAttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 补卡service实现类
 * @date 2023/4/19 19:19:49
 */
@Service
public class OffsetAttendanceServiceImpl extends ServiceImpl<OffsetAttendanceMapper, OffsetAttendance>implements OffsetAttendanceService {
}
