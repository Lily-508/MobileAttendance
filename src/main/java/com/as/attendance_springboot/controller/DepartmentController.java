package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.Department;
import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.model.enum_model.StaffRight;
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
import org.springframework.validation.ObjectError;
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
public class DepartmentController {
    @Autowired
    private DepartmentServiceImpl departmentService;
    @Autowired
    private StaffServiceImpl staffService;
    @GetMapping
    @ApiOperation("查询部门,查询条件:部门id,领导id")
    @ApiImplicitParams({@ApiImplicitParam(name = "dId", value = "部门id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "sId", value = "领导id", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class), @ApiResponse(code =
            400, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<Department>>> getDepartmentById(@RequestParam(required = false) Integer dId,
                                                                    @RequestParam(required = false) Integer sId) {
        DataResult<List<Department>> result = new DataResult<>();
        LambdaQueryWrapper<Department>queryWrapper = new LambdaQueryWrapper<>();
        if(dId!=null&&sId!=null){
            queryWrapper.eq(Department::getDId,dId).eq(Department::getSId,sId);
        }else if(dId!=null){
            queryWrapper.eq(Department::getDId,dId);
        }else if(sId!=null){
            queryWrapper.eq(Department::getSId,sId);
        }
        List<Department>list= departmentService.list(queryWrapper);
        if(list != null && list.size()!=0){
            result.setCode(200).setMsg("查询成功").setData(list);
        }else {
            result.setCode(400).setMsg("查询失败").setData(null);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建部门")
    @ApiImplicitParam(name = "department", value = "Department类实例", dataTypeClass = Department.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class), @ApiResponse(code =
            400, message = "新建失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setDepartment(@Valid @RequestBody Department department,
                                                    BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        Integer sId=department.getSId();
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage());
            }
            result.setCode(400).setMsg(errorMsg.toString());
        }else if(isErrorStaffId(sId)){
            result.setCode(400).setMsg("负责人id没有对应权限");
        }else {
            boolean success=departmentService.save(department);
            if(success){
                result.setCode(200).setMsg("新建成功");
            }else {
                result.setCode(400).setMsg("新建失败");
            }
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping
    @ApiOperation("编辑部门")
    @ApiImplicitParam(name = "department", value = "Department类实例", dataTypeClass = Department.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功", response = BaseResult.class), @ApiResponse(code =
            400, message = "编辑失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> updateDepartment(@Valid @RequestBody Department department,
                                                       BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        Integer sId=department.getSId();
        if(department.getDId()==null){
            result.setCode(400).setMsg("dId不能为null");
        }else if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage());
            }
            result.setCode(400).setMsg(errorMsg.toString());
        }else if(isErrorStaffId(sId)){
            result.setCode(400).setMsg("负责人id没有对应权限");
        }else{
            boolean success=departmentService.updateById(department);
            if(success){
                result.setCode(200).setMsg("编辑成功");
            }else {
                result.setCode(400).setMsg("编辑失败");
            }
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }
    /**
     * 判断StaffId有无领导权限
     * @author xulili
     * @date 14:26 2023/4/11
     * @param sId 负责人id
     * @return boolean
     **/
    private boolean isErrorStaffId(Integer sId){
        if(sId!=null){
            LambdaQueryWrapper<Staff> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Staff::getSId,sId).eq(Staff::getSRight, StaffRight.LEADER);
            Staff staff= staffService.getOne(queryWrapper);
            return staff == null;
        }else {
            return false;
        }
    }
}
