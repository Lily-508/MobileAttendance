package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.WorkOvertime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 加班事务数据接口层
 * @date 2023/4/19 16:07:30
 */
@Mapper
public interface WorkOvertimeMapper extends BaseMapper<WorkOvertime> {
}
