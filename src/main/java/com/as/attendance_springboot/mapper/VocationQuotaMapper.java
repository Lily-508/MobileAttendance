package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.VocationQuota;
import com.github.jeffreyning.mybatisplus.base.MppBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 假期额度数据操作层
 * @date 2023/4/12 17:59:11
 */
@Mapper
public interface VocationQuotaMapper extends MppBaseMapper<VocationQuota> {
}
