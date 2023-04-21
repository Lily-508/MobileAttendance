package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.Department;
import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.model.enums.StaffRight;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.DepartmentServiceImpl;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 部门接口
 * @date 2023/4/11 13:12:40
 */
@RestController
@RequestMapping("/departments")
@Api(tags = "部门管理接口,提供部门新建,修改,查询操作")
@Slf4j
public class DepartmentController extends BaseController {
    @Autowired
    private DepartmentServiceImpl departmentService;
    @Autowired
    private StaffServiceImpl staffService;

    //拜访表service
    @GetMapping
    @ApiOperation("查询部门,查询条件:部门id,领导id")
    @ApiImplicitParams({@ApiImplicitParam(name = "dId", value = "部门id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "sId", value = "领导id", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class),
            @ApiResponse(code = 500, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<Department>>> getDepartmentById(@RequestParam(required = false) Integer dId,
                                                                          @RequestParam(required = false) Integer sId) {
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        if (dId != null) {
            //根据部门号查询
            queryWrapper.eq(Department::getDId, dId);
        }
        if (sId != null) {
            //根据领导id查询
            queryWrapper.eq(Department::getSId, sId);
        }
        List<Department> list = departmentService.list(queryWrapper);
        DataResult<List<Department>> result = super.getModel(list);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建部门")
    @ApiImplicitParam(name = "department", value = "Department类实例", dataTypeClass = Department.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class),
            @ApiResponse(code = 500, message = "新建失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setDepartment(@Valid @RequestBody Department department,
                                                    BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        Integer sId = department.getSId();
        if (isErrorStaffId(sId)) {
            result.setCode(400).setMsg("负责人id没有对应权限");
        } else {
            result = super.setModel(departmentService, department, bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping
    @ApiOperation("编辑部门")
    @ApiImplicitParam(name = "department", value = "Department类实例", dataTypeClass = Department.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功", response = BaseResult.class),
            @ApiResponse(code = 500, message = "编辑失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> updateDepartment(@Valid @RequestBody Department department,
                                                       BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        Integer sId = department.getSId();
        if (isErrorStaffId(sId)) {
            result.setCode(400).setMsg("负责人id没有对应权限");
        } else {
            result = super.updateModelBySingle(department.getDId(), departmentService, department, bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @DeleteMapping
    @ApiOperation("删除没有外键依赖部门")
    @ApiImplicitParam(name = "dId", value = "部门id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功", response = BaseResult.class),
            @ApiResponse(code = 500, message = "删除失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> deleteDepartment(@RequestParam Integer dId) {
        //外键依赖员工表,拜访计划表
        LambdaQueryWrapper<Staff> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Staff::getDId, dId);
        Staff staff = staffService.getOne(queryWrapper);
        BaseResult result = super.deleteModel(staff == null);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    /**
     * 判断StaffId有无领导权限
     * @param sId 负责人id
     * @return boolean
     * @author xulili
     * @date 14:26 2023/4/11
     **/
    private boolean isErrorStaffId(Integer sId) {
        if (sId != null) {
            LambdaQueryWrapper<Staff> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Staff::getSId, sId).eq(Staff::getSRight, StaffRight.LEADER);
            Staff staff = staffService.getOne(queryWrapper);
            return staff == null;
        } else {
            return false;
        }
    }
}
