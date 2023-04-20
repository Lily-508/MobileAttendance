package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.WorkOutside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 外派事务数据操作层
 * @date 2023/4/18 17:06:19
 */
@Mapper
public interface WorkOutsideMapper extends BaseMapper<WorkOutside> {
}
