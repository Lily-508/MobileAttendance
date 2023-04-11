package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 部门表数据操作层
 * @date 2023/4/11 10:13:39
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
