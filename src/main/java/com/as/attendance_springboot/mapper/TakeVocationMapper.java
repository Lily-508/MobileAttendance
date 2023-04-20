package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.TakeVocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 请休假事务数据操作层
 * @date 2023/4/19 16:08:23
 */
@Mapper
public interface TakeVocationMapper extends BaseMapper<TakeVocation> {
}
