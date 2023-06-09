package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.AttendanceRule;
import com.as.attendance_springboot.model.RecordAttendance;
import com.as.attendance_springboot.model.WorkOutside;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.AttendanceRuleServiceImpl;
import com.as.attendance_springboot.service.impl.CompanyServiceImpl;
import com.as.attendance_springboot.service.impl.RecordAttendanceServiceImpl;
import com.as.attendance_springboot.service.impl.WorkOutsideServiceImpl;
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
 * @description 考勤规则接口
 * @date 2023/4/13 11:52:34
 */
@Api(tags = "考勤规则接口,提供查询,新建,更改,删除")
@RestController
@RequestMapping("/attendances/rules")
@Slf4j
public class AttendanceRuleController extends BaseController {
    @Autowired
    private AttendanceRuleServiceImpl attendanceRuleService;
    @Autowired
    private CompanyServiceImpl companyService;
    @Autowired
    private RecordAttendanceServiceImpl recordAttendanceService;
    @Autowired
    private WorkOutsideServiceImpl workOutsideService;

    @GetMapping("/list")
    @ApiOperation("查询列表考勤规则,查询条件:公司id")
    @ApiImplicitParam(name = "cId", value = "公司id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class),
            @ApiResponse(code = 500, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<AttendanceRule>>> getAttendanceRuleByCid(@RequestParam(required = false) Integer cId) {
        LambdaQueryWrapper<AttendanceRule> queryWrapper = new LambdaQueryWrapper<>();
        if (cId != null) {
            queryWrapper.eq(AttendanceRule::getCId, cId);
        }
        List<AttendanceRule> list = attendanceRuleService.list(queryWrapper);
        DataResult<List<AttendanceRule>> result = super.getModel(list);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @GetMapping
    @ApiOperation("查询单个考勤规则,查询条件:考勤规则id")
    @ApiImplicitParam(name = "aId", value = "考勤规则id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class),
            @ApiResponse(code = 500, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<AttendanceRule>> getAttendanceRuleByAid(@RequestParam(required = false) Integer aId) {
        AttendanceRule attendanceRule = attendanceRuleService.getById(aId);
        DataResult<AttendanceRule> result = super.getModel(attendanceRule);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建考勤规则")
    @ApiImplicitParam(name = "attendanceRule", value = "AttendanceRule类实例", dataTypeClass = AttendanceRule.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class),
            @ApiResponse(code = 400, message = "错误的公司id", response = BaseResult.class),
            @ApiResponse(code = 500, message = "新建失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setAttendanceRule(@Valid @RequestBody AttendanceRule attendanceRule,
                                                        BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (isErrorCompanyId(attendanceRule)) {
            result.setCode(400).setMsg("错误的公司id");
        } else {
            super.setModel(attendanceRuleService, attendanceRule, bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping
    @ApiOperation("编辑考勤规则")
    @ApiImplicitParam(name = "attendanceRule", value = "AttendanceRule类实例", dataTypeClass = AttendanceRule.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功", response = BaseResult.class),
            @ApiResponse(code = 400, message = "错误的公司id", response = BaseResult.class),
            @ApiResponse(code = 500, message = "编辑失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> updateDepartment(@Valid @RequestBody AttendanceRule attendanceRule,
                                                       BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (isErrorCompanyId(attendanceRule)) {
            result.setCode(400).setMsg("错误的公司id");
        } else {
            super.updateModelBySingle(attendanceRule.getAId(), attendanceRuleService, attendanceRule, bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @DeleteMapping
    @ApiOperation("删除没有外键依赖考勤规则")
    @ApiImplicitParam(name = "aId", value = "考勤规则id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功", response = BaseResult.class),
            @ApiResponse(code = 400, message = "已被外键依赖", response = BaseResult.class),
            @ApiResponse(code = 500, message = "删除失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> deleteDepartment(@RequestParam Integer aId) {
        //被考勤记录表,外派事务外键依赖
        BaseResult result = new BaseResult();
        RecordAttendance recordAttendance = recordAttendanceService.getOne(
                new LambdaQueryWrapper<RecordAttendance>().eq(RecordAttendance::getAId, aId));
        WorkOutside workOutside = workOutsideService.getOne(
                new LambdaQueryWrapper<WorkOutside>().eq(WorkOutside::getAId, aId));
        if (recordAttendance != null || workOutside != null) {
            result.setCode(400).setMsg("已被外键依赖");
        } else {
            super.deleteModel(attendanceRuleService.removeById(aId));
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    private boolean isErrorCompanyId(AttendanceRule attendanceRule) {
        return attendanceRule.getCId() == null || companyService.getById(attendanceRule.getCId()) == null;
    }
}
