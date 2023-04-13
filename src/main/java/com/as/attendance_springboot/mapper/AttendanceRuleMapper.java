package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.AttendanceRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 考勤规则数据操作层
 * @date 2023/4/13 10:51:22
 */
@Mapper
public interface AttendanceRuleMapper extends BaseMapper<AttendanceRule> {
}
