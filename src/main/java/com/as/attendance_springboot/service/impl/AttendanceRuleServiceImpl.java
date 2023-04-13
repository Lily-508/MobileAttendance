package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.AttendanceRuleMapper;
import com.as.attendance_springboot.model.AttendanceRule;
import com.as.attendance_springboot.service.AttendanceRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 考勤规则service实现
 * @date 2023/4/13 10:55:49
 */
@Service
public class AttendanceRuleServiceImpl extends ServiceImpl<AttendanceRuleMapper, AttendanceRule>implements AttendanceRuleService {
}
