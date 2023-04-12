package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.Holiday;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 假期表数据操作层
 * @date 2023/4/12 13:56:05
 */
@Mapper
public interface HolidayMapper extends BaseMapper<Holiday> {
}
