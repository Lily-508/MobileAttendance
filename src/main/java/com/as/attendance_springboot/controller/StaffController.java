package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.service.StaffService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xulili
 */
@RestController
@RequestMapping("/staffs")
@Api(tags = "员工管理接口,提供员工新建,修改,查询,和删除操作")
@Slf4j
public class StaffController {
    @Autowired
    private StaffService staffService;

}
