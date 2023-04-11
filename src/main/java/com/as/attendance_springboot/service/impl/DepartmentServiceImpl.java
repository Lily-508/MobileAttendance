package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.DepartmentMapper;
import com.as.attendance_springboot.model.Department;
import com.as.attendance_springboot.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 部门service实现类
 * @date 2023/4/11 10:16:56
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper,Department> implements DepartmentService {

}
