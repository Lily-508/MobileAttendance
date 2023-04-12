package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.VocationQuotaMapper;
import com.as.attendance_springboot.model.VocationQuota;
import com.as.attendance_springboot.service.VocationQuotaService;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 假期额度service实现
 * @date 2023/4/12 18:04:47
 */
@Service
public class VocationQuotaServiceImpl extends MppServiceImpl<VocationQuotaMapper, VocationQuota> implements VocationQuotaService {

}
