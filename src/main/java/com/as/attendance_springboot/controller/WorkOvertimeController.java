package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.model.VocationQuota;
import com.as.attendance_springboot.model.WorkOvertime;
import com.as.attendance_springboot.model.enums.AuditType;
import com.as.attendance_springboot.model.enums.StaffRight;
import com.as.attendance_springboot.model.enums.VocationType;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
import com.as.attendance_springboot.service.impl.VocationQuotaServiceImpl;
import com.as.attendance_springboot.service.impl.WorkOvertimeServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
 * @description 加班事务接口
 * @date 2023/4/19 19:23:44
 */
@Api(tags = "加班事务接口,查询,新建,更改,删除")
@RestController
@RequestMapping("/work-overtime")
@Slf4j
public class WorkOvertimeController extends BaseController {
    @Autowired
    private WorkOvertimeServiceImpl workOvertimeService;
    @Autowired
    private StaffServiceImpl staffService;
    @Autowired
    private VocationQuotaServiceImpl vocationQuotaService;

    @GetMapping
    @ApiOperation("查询加班事务,查询条件:加班事务id,员工id,公司id")
    @ApiImplicitParams({@ApiImplicitParam(name = "wvId", value = "加班事务id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "sId", value = "员工id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "reviewerId", value = "审核人id", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class),
            @ApiResponse(code = 500, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<WorkOvertime>>> getWorkOvertime(@RequestParam(required = false) Integer wvId,
                                                                          @RequestParam(required = false) Integer sId,
                                                                          @RequestParam(required = false) Integer reviewerId) {
        DataResult<List<WorkOvertime>> result;
        LambdaQueryWrapper<WorkOvertime> queryWrapper = new LambdaQueryWrapper<>();
        if (wvId != null) {
            queryWrapper.eq(WorkOvertime::getWvId, wvId);
        }
        if (sId != null) {
            queryWrapper.eq(WorkOvertime::getSId, sId);
        }
        if (reviewerId != null) {
            queryWrapper.eq(WorkOvertime::getReviewer, reviewerId);
        }
        List<WorkOvertime> list = workOvertimeService.list(queryWrapper);
        result = super.getModel(list);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建加班事务")
    @ApiImplicitParam(name = "workOvertime", value = "WorkOvertime类实例", dataTypeClass = WorkOvertime.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class),
            @ApiResponse(code = 400, message = "错误的外键id", response = BaseResult.class),
            @ApiResponse(code = 500, message = "新建失败", response = BaseResult.class),
            @ApiResponse(code = 400, message = "错误的时间", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setWorkOvertime(@NotNull @Valid @RequestBody WorkOvertime workOvertime,
                                                      BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (isErrorForeignId(workOvertime)) {
            result.setCode(400).setMsg("错误的外键id");
        } else if (super.isErrorTime(workOvertime)) {
            result.setCode(400).setMsg("错误的时间");
        } else {
            result = super.setModel(workOvertimeService, workOvertime, bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping
    @ApiOperation("编辑加班事务")
    @ApiImplicitParam(name = "workOvertime", value = "对应WorkOvertime的JSON数据", dataTypeClass = String.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功"),
            @ApiResponse(code = 400, message = "错误的外键id"),
            @ApiResponse(code = 400, message = "错误的时间"),
            @ApiResponse(code = 500, message = "编辑失败")})
    public ResponseEntity<BaseResult> updateWorkOvertime(@NotNull @Valid @RequestBody WorkOvertime workOvertime,
                                                         BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (isErrorForeignId(workOvertime)) {
            result.setCode(400).setMsg("错误的外键id");
        } else if (super.isErrorTime(workOvertime)) {
            result.setCode(400).setMsg("错误的时间");
        } else {
            result = super.updateModelBySingle(workOvertime.getWvId(), workOvertimeService, workOvertime,
                                               bindingResult);
            //加班事务通过增加调休假期额度
            if (result.getCode() == 200 && workOvertime.getResult() == AuditType.AGREE) {
                VocationQuota vocationQuota = new VocationQuota();
                vocationQuota.setVId(VocationType.COMPENSATORY)
                             .setSId(workOvertime.getSId())
                             .setVDuration(workOvertime.getTotal());
                vocationQuotaService.updateByMultiId(vocationQuota);
            }
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @DeleteMapping
    @ApiOperation("删除未通过的加班事务")
    @ApiImplicitParam(name = "wvId", value = "加班事务id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功", response = BaseResult.class),
            @ApiResponse(code = 200, message = "对应事务不存在或已审核通过", response = BaseResult.class),
            @ApiResponse(code = 500, message = "删除失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> deleteWorkOvertime(@RequestParam Integer wvId) {
        BaseResult result = new BaseResult();
        WorkOvertime workOvertime = workOvertimeService.getById(wvId);
        if (workOvertime == null || workOvertime.getResult() == AuditType.AGREE) {
            result.setCode(400).setMsg("对应事务不存在或已审核通过");
        } else {
            result = super.deleteModel(workOvertimeService.removeById(wvId));
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    private boolean isErrorForeignId(WorkOvertime workOvertime) {
        Staff staff = staffService.getById(workOvertime.getSId());
        Staff reviewer = staffService.getById(workOvertime.getReviewer());
        return staff == null || reviewer == null || reviewer.getSRight() != StaffRight.LEADER;
    }

}
