package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.OffsetAttendance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 补卡事务数据接口层
 * @date 2023/4/19 16:09:15
 */
@Mapper
public interface OffsetAttendanceMapper extends BaseMapper<OffsetAttendance> {
}
