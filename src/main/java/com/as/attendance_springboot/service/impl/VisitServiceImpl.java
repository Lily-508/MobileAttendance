package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.VisitMapper;
import com.as.attendance_springboot.model.Visit;
import com.as.attendance_springboot.service.VisitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 拜访表Service实现
 * @date 2023/4/18 09:29:11
 */
@Service
public class VisitServiceImpl extends ServiceImpl<VisitMapper, Visit> implements VisitService {
}
