package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.WorkOvertimeMapper;
import com.as.attendance_springboot.model.WorkOvertime;
import com.as.attendance_springboot.service.WorkOvertimeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 加班事务service实现类
 * @date 2023/4/19 19:22:20
 */
@Service
public class WorkOvertimeServiceImpl extends ServiceImpl<WorkOvertimeMapper, WorkOvertime>implements WorkOvertimeService {
}
