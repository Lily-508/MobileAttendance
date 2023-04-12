package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.Company;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 公司表数据操作层
 * @date 2023/4/11 18:41:03
 */
@Mapper
public interface CompanyMapper extends BaseMapper<Company> {
}
