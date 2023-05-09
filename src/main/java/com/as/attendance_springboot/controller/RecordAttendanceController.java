package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.AttendanceRule;
import com.as.attendance_springboot.model.RecordAttendance;
import com.as.attendance_springboot.model.WorkOutside;
import com.as.attendance_springboot.model.enums.RecordCategoryType;
import com.as.attendance_springboot.model.enums.RecordResultType;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.result.PaginationResult;
import com.as.attendance_springboot.service.impl.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 考勤记录接口
 * @date 2023/4/13 12:17:11
 */
@Api(tags = "考勤记录接口,提供查询,新建,更改;当对应员工删除自动删除")
@RestController
@RequestMapping("/attendances/records")
@Slf4j
public class RecordAttendanceController extends BaseController {
    @Autowired
    private RecordAttendanceServiceImpl recordAttendanceService;
    @Autowired
    private AttendanceRuleServiceImpl attendanceRuleService;
    @Autowired
    private StaffServiceImpl staffService;
    @Autowired
    private CompanyServiceImpl companyService;
    @Autowired
    private WorkOutsideServiceImpl workOutsideService;

    @GetMapping
    @ApiOperation("查询单个考勤记录,查询条件:考勤记录id")
    @ApiImplicitParams({@ApiImplicitParam(name = "rId", value = "考勤记录id", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class),
            @ApiResponse(code = 500, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<RecordAttendance>> getRecordAttendanceByRid(@RequestParam Integer rId) {
        DataResult<RecordAttendance> result = super.getModel(recordAttendanceService.getById(rId));
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询考勤记录,查询条件:员工id")
    @ApiImplicitParams({@ApiImplicitParam(name = "sId", value = "员工id", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = PaginationResult.class)})
    public ResponseEntity<PaginationResult<IPage<RecordAttendance>>> getRecordAttendancePageBySid(
            @RequestParam Integer pageCur,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) Integer sId) {
        LambdaQueryWrapper<RecordAttendance> queryWrapper = new LambdaQueryWrapper<>();
        if (sId != null) {
            queryWrapper.eq(RecordAttendance::getSId, sId);
        }
        IPage<RecordAttendance> page = recordAttendanceService.page(new Page(pageCur, pageSize), queryWrapper);
        PaginationResult<IPage<RecordAttendance>> result = super.getModelPage(page);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping("/punch-in")
    @ApiOperation("考勤签到")
    @ApiImplicitParam(name = "recordAttendance", value = "RecordAttendance类实例", dataTypeClass = RecordAttendance.class)
    @ApiResponses({@ApiResponse(code = 200, message = "签到成功", response = DataResult.class),
            @ApiResponse(code = 500, message = "签到失败", response = DataResult.class),
            @ApiResponse(code = 400, message = "错误的外键id", response = DataResult.class),
            @ApiResponse(code = 400, message = "没有对应外派事务", response = DataResult.class),
            @ApiResponse(code = 400, message = "签到参数格式错误", response = DataResult.class),
            @ApiResponse(code = 400, message = "签到操作只能进行一次", response = DataResult.class)})
    public ResponseEntity<DataResult<RecordAttendance>> recordPunchIn(
            @NotNull @Valid @RequestBody RecordAttendance recordAttendance,
            BindingResult bindingResult) {
        DataResult<RecordAttendance> result = new DataResult<>();
        if (!super.validBindingResult(bindingResult, result)) {
            return ResponseEntity.status(result.getCode()).body(result);
        } else if (isErrorForeignId(recordAttendance)) {
            result.setCode(400).setMsg("错误的外键id");
        } else if (recordAttendance.getRPunchIn() == null || recordAttendance.getPunchInPlace() == null ||
                recordAttendance.getRDate() == null || recordAttendance.getRCategory() == null) {
            result.setCode(400).setMsg("签到参数格式错误");
        } else if (!isCorrectWorkOutside(recordAttendance)) {
            result.setCode(400).setMsg("没有对应外派事务");
        } else {
            LambdaQueryWrapper<RecordAttendance> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RecordAttendance::getSId, recordAttendance.getSId())
                        .eq(RecordAttendance::getRDate, LocalDate.now())
                        .eq(RecordAttendance::getRCategory, recordAttendance.getRCategory());
            RecordAttendance record = recordAttendanceService.getOne(queryWrapper);
            if (record != null) {
                result.setCode(400).setMsg("签到操作只能进行一次");
            } else if (recordAttendanceService.saveOrUpdate(recordAttendance)) {
                result.setCode(200).setMsg("签到成功").setData(recordAttendance);
            } else {
                result.setCode(500).setMsg("签到失败");
            }
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping("/punch-out")
    @ApiOperation("考勤签退")
    @ApiImplicitParam(name = "recordAttendance", value = "RecordAttendance类实例", dataTypeClass = RecordAttendance.class)
    @ApiResponses({@ApiResponse(code = 200, message = "签退成功", response = DataResult.class),
            @ApiResponse(code = 500, message = "签退失败", response = DataResult.class),
            @ApiResponse(code = 400, message = "错误的外键id", response = DataResult.class),
            @ApiResponse(code = 400, message = "请先签到", response = BaseResult.class),
            @ApiResponse(code = 400, message = "签退参数格式错误", response = DataResult.class),
            @ApiResponse(code = 400, message = "签退操作只能进行一次", response = DataResult.class)})
    public ResponseEntity<DataResult<RecordAttendance>> recordPunchOut(@NotNull @Valid @RequestBody RecordAttendance recordAttendance,
                                                                       BindingResult bindingResult) {
        DataResult<RecordAttendance> result = new DataResult<>();
        if (!super.validBindingResult(bindingResult, result)) {
            return ResponseEntity.status(result.getCode()).body(result);
        } else if (recordAttendance.getRPunchOut() == null || recordAttendance.getPunchOutPlace() == null ||
                recordAttendance.getRId() == null) {
            result.setCode(400).setMsg("签退参数格式错误");
        } else {
            RecordAttendance record = recordAttendanceService.getById(recordAttendance.getRId());
            if (record == null) {
                result.setCode(400).setMsg("请先签到");
            } else if (record.getPunchOutPlace() != null) {
                result.setCode(400).setMsg("签退操作只能进行一次");
            } else {
                decideRecordResultByTime(recordAttendance);
                if (recordAttendanceService.saveOrUpdate(recordAttendance)) {
                    result.setCode(200).setMsg("签退成功").setData(recordAttendance);
                } else {
                    result.setCode(500).setMsg("签退失败");
                }
            }
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    private boolean isErrorForeignId(RecordAttendance recordAttendance) {
        log.info("外键有效性判断:无效,员工id={},考勤规则id={}",
                 recordAttendance.getSId(),
                 recordAttendance.getAId());
        if (recordAttendance.getSId() != null && recordAttendance.getAId() != null) {
            return staffService.getById(recordAttendance.getSId()) == null || attendanceRuleService.getById(
                    recordAttendance.getAId()) == null;
        } else {
            return true;
        }
    }

    private boolean isCorrectWorkOutside(RecordAttendance recordAttendance) {
        boolean correct;
        if (recordAttendance.getRCategory() == RecordCategoryType.OUTSIDE) {
            LambdaQueryWrapper<WorkOutside> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WorkOutside::getAId, recordAttendance.getAId())
                        .eq(WorkOutside::getSId, recordAttendance.getSId());
            log.info("检查是否有对应外派事务");
            List<WorkOutside> workOutsideList = workOutsideService.list(queryWrapper);
            correct=workOutsideList.size()>0;
        } else {
            correct = true;
        }
        return correct;
    }

    private void decideRecordResultByTime(RecordAttendance recordAttendance) {
        //签到判断地点是否在范围内
        if (recordAttendance.getRResult() == null || recordAttendance.getRResult() == RecordResultType.ON ||
                recordAttendance.getRResult() == RecordResultType.SUCCESS) {
            AttendanceRule attendanceRule = attendanceRuleService.getById(recordAttendance.getAId());
            LocalTime punchIn = recordAttendance.getRPunchIn().toLocalTime();
            LocalTime punchOut = recordAttendance.getRPunchOut().toLocalTime();
            LocalTime start = attendanceRule.getAStart();
            LocalTime end = attendanceRule.getAEnd();
            int exceptionRange = attendanceRule.getExceptionRange();
            int neglectRange = attendanceRule.getNeglectRange();
            Duration punchInDuration = Duration.between(start, punchIn);
            Duration punchOutDuration = Duration.between(punchOut, end);
            log.info("考勤结果时间判断,签到时差{},签退时差{}", punchInDuration.toMinutes(),
                     punchOutDuration.toMinutes());
            if (punchInDuration.toMinutes() < exceptionRange && punchOutDuration.toMinutes() < exceptionRange &&
                    recordAttendance.getRResult() == RecordResultType.SUCCESS) {
                recordAttendance.setRResult(RecordResultType.SUCCESS);
            } else if (punchInDuration.toMinutes() > neglectRange || punchOutDuration.toMinutes() > neglectRange) {
                recordAttendance.setRResult(RecordResultType.NEGLECT);
            } else {
                recordAttendance.setRResult(RecordResultType.EXCEPTION);
            }
        }
    }
}
