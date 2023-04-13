package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.RecordAttendance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 考勤记录数据操作层
 * @date 2023/4/13 10:52:36
 */
@Mapper
public interface RecordAttendanceMapper extends BaseMapper<RecordAttendance> {
}
