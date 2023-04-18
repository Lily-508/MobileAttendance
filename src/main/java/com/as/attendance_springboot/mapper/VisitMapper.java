package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.Visit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 拜访表数据操作层
 * @date 2023/4/18 09:23:26
 */
@Mapper
public interface VisitMapper extends BaseMapper<Visit> {
}
