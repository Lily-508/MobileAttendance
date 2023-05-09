package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.AttendanceRule;
import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.model.WorkOutside;
import com.as.attendance_springboot.model.enums.AuditType;
import com.as.attendance_springboot.model.enums.StaffRight;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.AttendanceRuleServiceImpl;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
import com.as.attendance_springboot.service.impl.WorkOutsideServiceImpl;
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
 * @description 外派事务Controller层
 * @date 2023/4/18 17:09:10
 */
@Api(tags = "外派事务接口,查询,新建,更改,删除")
@RestController
@RequestMapping("/work-outside")
@Slf4j
public class WorkOutsideController extends BaseController {
    @Autowired
    private WorkOutsideServiceImpl workOutsideService;
    @Autowired
    private StaffServiceImpl staffService;
    @Autowired
    private AttendanceRuleServiceImpl attendanceRuleService;

    @GetMapping
    @ApiOperation("查询外派事务,查询条件:外派事务id,员工id,公司id")
    @ApiImplicitParams({@ApiImplicitParam(name = "wuId", value = "外派事务id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "sId", value = "员工id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "reviewerId", value = "审核人id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "cId", value = "公司id", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class),
            @ApiResponse(code = 500, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<WorkOutside>>> getWorkOutside(@RequestParam(required = false) Integer wuId,
                                                                        @RequestParam(required = false) Integer sId,
                                                                        @RequestParam(required = false) Integer reviewerId,
                                                                        @RequestParam(required = false) Integer cId) {
        DataResult<List<WorkOutside>> result;
        LambdaQueryWrapper<WorkOutside> queryWrapper = new LambdaQueryWrapper<>();
        if (wuId != null) {
            queryWrapper.eq(WorkOutside::getWuId, wuId);
        }
        if (sId != null) {
            queryWrapper.eq(WorkOutside::getSId, sId);
        }
        if (cId != null) {
            queryWrapper.inSql(WorkOutside::getAId,"select a_id from `attendance_rule` where c_id="+cId);
        }
        if (reviewerId != null) {
            queryWrapper.eq(WorkOutside::getReviewer, reviewerId);
        }
        List<WorkOutside> list = workOutsideService.list(queryWrapper);
        result = super.getModel(list);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建外派事务")
    @ApiImplicitParam(name = "workOutside", value = "WorkOutside类实例", dataTypeClass = WorkOutside.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class),
            @ApiResponse(code = 400, message = "错误的外键id", response = BaseResult.class),
            @ApiResponse(code = 500, message = "新建失败", response = BaseResult.class),
            @ApiResponse(code = 400, message = "错误的时间", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setWorkOutside(@NotNull @Valid @RequestBody WorkOutside workOutside,
                                                     BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        log.info(workOutside.toString());
        if (isErrorForeignId(workOutside)) {
            result.setCode(400).setMsg("错误的外键id");
        } else if (super.isErrorTime(workOutside)) {
            result.setCode(400).setMsg("错误的时间");
        } else {
            result = super.setModel(workOutsideService, workOutside, bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping
    @ApiOperation("编辑外派事务")
    @ApiImplicitParam(name = "workOutside", value = "对应WorkOutside的JSON数据", dataTypeClass = String.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功"),
            @ApiResponse(code = 400, message = "错误的外键id"),
            @ApiResponse(code = 400, message = "错误的时间"),
            @ApiResponse(code = 500, message = "编辑失败")})
    public ResponseEntity<BaseResult> updateWorkOutside(@NotNull @Valid @RequestBody WorkOutside workOutside,
                                                        BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (isErrorForeignId(workOutside)) {
            result.setCode(400).setMsg("错误的外键id");
        } else if (super.isErrorTime(workOutside)) {
            result.setCode(400).setMsg("错误的时间");
        } else {
            result = super.updateModelBySingle(workOutside.getWuId(), workOutsideService, workOutside, bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @DeleteMapping
    @ApiOperation("删除未通过的外派事务")
    @ApiImplicitParam(name = "wuId", value = "外派事务id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功", response = BaseResult.class),
            @ApiResponse(code = 200, message = "对应事务不存在或已审核通过", response = BaseResult.class),
            @ApiResponse(code = 500, message = "删除失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> deleteWorkOutside(@RequestParam Integer wuId) {
        BaseResult result = new BaseResult();
        WorkOutside workOutside = workOutsideService.getById(wuId);
        if (workOutside == null || workOutside.getResult() == AuditType.AGREE) {
            result.setCode(400).setMsg("对应事务不存在或已审核通过");
        } else {
            result = super.deleteModel(workOutsideService.removeById(wuId));
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    private boolean isErrorForeignId(WorkOutside workOutside) {
        Staff staff = staffService.getById(workOutside.getSId());
        Staff reviewer = staffService.getById(workOutside.getReviewer());
        AttendanceRule attendanceRule = attendanceRuleService.getById(workOutside.getAId());
        return staff == null || reviewer == null || attendanceRule == null || reviewer.getSRight() != StaffRight.LEADER;
    }
}
