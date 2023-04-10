package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.result.PaginationResult;
import com.as.attendance_springboot.service.StaffService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

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

    @GetMapping("/page")
    @ApiOperation("分页查询对应部门员工")
    @ApiImplicitParams({@ApiImplicitParam(name = "dId", value = "部门id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageCur", value = "当前页数", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", dataTypeClass = Integer.class)})
    @ApiResponse(code = 200, message = "查询成功", response = PaginationResult.class)
    public ResponseEntity<PaginationResult<IPage<Staff>>> getStaffPageByDepartmentId(@RequestParam int dId,
                                                                                     @RequestParam int pageCur,
                                                                                     @RequestParam int pageSize) {
        log.info("传入参数部门id={},当前页数={},页面大小={}", dId, pageCur, pageSize);
        LambdaQueryWrapper<Staff> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Staff::getDId, dId);
        IPage<Staff> page = staffService.page(new Page(pageCur, pageSize), lambdaQueryWrapper);
        PaginationResult<IPage<Staff>> result = new PaginationResult<>();
        result.setCode(200).setMsg("查询成功").setData(page).setTotal(page.getTotal());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/excel")
    @ApiOperation("excel员工表导入数据库")
    @ApiImplicitParam(name = "file", value = "excel文件", dataTypeClass = MultipartFile.class)
    @ApiResponse(code = 200, message = "导入成功")
    public ResponseEntity<BaseResult> importStaffExcel(MultipartFile file) throws IOException {
        boolean success = staffService.importStaffExcel(file);
        BaseResult result = new BaseResult();
        if (success) {
            result.setCode(200).setMsg("导入成功");
        } else {
            result.setCode(400).setMsg("导入失败");
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @GetMapping("/excel")
    @ApiOperation("导出excel员工表")
    public void exportStaffExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LocalDate localDate = LocalDate.now();
        String fileName = "员工表" + localDate + ".xlsx";
        log.info(fileName);
        try {
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            staffService.exportStaffExcel(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    @ApiOperation("根据sId查询对应员工")
    @ApiImplicitParam(name = "sId", value = "员工id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class), @ApiResponse(code =
            400, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<Staff>> getStaffByStaffId(@RequestParam int sId) {
        log.info("传入参数员工id={}", sId);
        Staff staff = staffService.getById(sId);
        DataResult<Staff> result = new DataResult<>();
        if (staff == null) {
            result.setCode(400).setMsg("查询失败").setData(null);
        } else {
            staff.setSPwd(null);
            result.setCode(200).setMsg("查询成功").setData(staff);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建员工")
    @ApiImplicitParam(name = "staff", value = "必填dId,sName,sPwd", dataTypeClass = Staff.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功"), @ApiResponse(code = 400, message = "新建失败")})
    public ResponseEntity<BaseResult> setStaff(@Valid @RequestBody Staff staff, BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage());
            }
            result.setCode(400).setMsg(errorMsg.toString());
        } else {
            boolean success = staffService.save(staff);
            if (success) {
                result.setCode(200).setMsg("新建成功");
            } else {
                result.setCode(400).setMsg("新建失败");
            }
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping
    @ApiOperation("编辑员工")
    @ApiImplicitParam(name = "staff", value = "编辑后的Staff,sId必填", dataTypeClass = Staff.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功"), @ApiResponse(code = 400, message = "编辑失败")})
    public ResponseEntity<BaseResult> updateStaff(@Valid @RequestBody Staff staff, BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage());
            }
            result.setCode(400).setMsg(errorMsg.toString());
        } else {
            boolean success = staffService.updateById(staff);
            if (success) {
                result.setCode(200).setMsg("编辑成功");
            } else {
                result.setCode(400).setMsg("编辑失败");
            }
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @DeleteMapping
    @ApiOperation("删除员工")
    @ApiImplicitParam(name = "sId", value = "员工id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功"), @ApiResponse(code = 400, message = "删除失败")})
    public ResponseEntity<BaseResult> deleteStaffByStaffId(@RequestParam int sId) {
        BaseResult result = new BaseResult();
        boolean success = staffService.removeById(sId);
        if (success) {
            result.setCode(200).setMsg("删除成功");
        } else {
            result.setCode(400).setMsg("删除失败");
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }
}
