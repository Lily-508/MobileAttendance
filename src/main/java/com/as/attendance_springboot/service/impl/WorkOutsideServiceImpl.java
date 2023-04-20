package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.WorkOutsideMapper;
import com.as.attendance_springboot.model.WorkOutside;
import com.as.attendance_springboot.service.WorkOutsideService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 外派事务service实现
 * @date 2023/4/18 17:08:05
 */
@Service
public class WorkOutsideServiceImpl extends ServiceImpl<WorkOutsideMapper, WorkOutside>implements WorkOutsideService {
}
