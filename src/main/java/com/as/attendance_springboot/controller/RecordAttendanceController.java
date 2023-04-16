package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.AttendanceRule;
import com.as.attendance_springboot.model.RecordAttendance;
import com.as.attendance_springboot.model.enums.RecordResultType;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.result.PaginationResult;
import com.as.attendance_springboot.service.impl.AttendanceRuleServiceImpl;
import com.as.attendance_springboot.service.impl.RecordAttendanceServiceImpl;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
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
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @GetMapping
    @ApiOperation("查询单个考勤记录,查询条件:考勤记录id")
    @ApiImplicitParams({@ApiImplicitParam(name = "rId", value = "考勤记录id", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class), @ApiResponse(code =
            400, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<RecordAttendance>> getRecordAttendanceByRid(@RequestParam Integer rId) {
        log.info("输入参数{}", rId);
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

    @PostMapping("/punch")
    @ApiOperation("考勤签到签退记录")
    @ApiImplicitParam(name = "recordAttendance", value = "RecordAttendance类实例", dataTypeClass = RecordAttendance.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class), @ApiResponse(code =
            400, message = "新建失败", response = BaseResult.class)})
    public ResponseEntity<DataResult<RecordAttendance>> setRecordAttendance(@NotNull @Valid @RequestBody RecordAttendance recordAttendance,
                                                                            BindingResult bindingResult) {
        DataResult<RecordAttendance> result=new DataResult<>();
        //外键有效判断
        if(isErrorForeignId(recordAttendance)){
            result.setCode(400).setMsg("错误的外键id");
        }else{
            //签到最少参数
            if (recordAttendance.getRPunchIn() == null && recordAttendance.getRDate() == null) {
                recordAttendance.setRDate(LocalDate.now());
                recordAttendance.setRPunchIn(LocalDateTime.now());
            }
            if (recordAttendance.getRDate() == null) {
                recordAttendance.setRDate(LocalDate.now());
            }
            //考勤结果判断
            if (recordAttendance.getRPunchOut() != null && recordAttendance.getRResult() == null) {
                decideRecordResultByTime(recordAttendance);
            }
            result = super.setModelAndReturn(recordAttendanceService, recordAttendance, bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    private boolean isErrorForeignId(RecordAttendance recordAttendance) {
        log.info("考勤记录判断外键id有效性");
        if (recordAttendance.getSId() != null && recordAttendance.getAId() != null) {
            return staffService.getById(recordAttendance.getSId()) == null || attendanceRuleService.getById(recordAttendance.getAId()) == null;
        }else {
            return true;
        }
    }

    private void decideRecordResultByTime(RecordAttendance recordAttendance) {
        AttendanceRule attendanceRule = attendanceRuleService.getById(recordAttendance.getAId());
        LocalTime punchIn = recordAttendance.getRPunchIn().toLocalTime();
        LocalTime punchOut = recordAttendance.getRPunchOut().toLocalTime();
        LocalTime start = attendanceRule.getAStart();
        LocalTime end = attendanceRule.getAEnd();
        int exceptionRange = attendanceRule.getExceptionRange();
        int neglectRange = attendanceRule.getNeglectRange();
        Duration punchInDuration = Duration.between(start, punchIn);
        Duration punchOutDuration = Duration.between(punchOut, end);
        log.info("签到时差{},签退时差{}", punchInDuration.toMinutes(), punchOutDuration.toMinutes());
        if (punchInDuration.toMinutes() < exceptionRange && punchOutDuration.toMinutes() < exceptionRange) {
            recordAttendance.setRResult(RecordResultType.SUCCESS);
        } else if (punchInDuration.toMinutes() > neglectRange || punchOutDuration.toMinutes() > neglectRange) {
            recordAttendance.setRResult(RecordResultType.NEGLECT);
        } else {
            recordAttendance.setRResult(RecordResultType.EXCEPTION);
        }
        //打卡地点是否在范围判断,打算交给前端
    }
}
