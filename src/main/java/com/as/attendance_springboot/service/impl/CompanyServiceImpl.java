package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.CompanyMapper;
import com.as.attendance_springboot.model.Company;
import com.as.attendance_springboot.service.CompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 公司模块service实现类
 * @date 2023/4/11 18:44:21
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
}
