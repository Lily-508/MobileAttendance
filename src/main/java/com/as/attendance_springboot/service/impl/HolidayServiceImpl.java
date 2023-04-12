package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.HolidayMapper;
import com.as.attendance_springboot.model.Holiday;
import com.as.attendance_springboot.service.HolidayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 假期接口实现
 * @date 2023/4/12 14:00:06
 */
@Service
public class HolidayServiceImpl extends ServiceImpl<HolidayMapper, Holiday>implements HolidayService {
}
