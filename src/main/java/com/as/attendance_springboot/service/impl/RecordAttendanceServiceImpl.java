package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.RecordAttendanceMapper;
import com.as.attendance_springboot.model.RecordAttendance;
import com.as.attendance_springboot.service.RecordAttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 考勤记录service实现
 * @date 2023/4/13 11:50:10
 */
@Service
public class RecordAttendanceServiceImpl extends ServiceImpl<RecordAttendanceMapper, RecordAttendance>implements RecordAttendanceService {
}
