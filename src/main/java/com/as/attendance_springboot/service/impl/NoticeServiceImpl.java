package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.NoticeMapper;
import com.as.attendance_springboot.model.Notice;
import com.as.attendance_springboot.service.NoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper,Notice > implements NoticeService {

}
