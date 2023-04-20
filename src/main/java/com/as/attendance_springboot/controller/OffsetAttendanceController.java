package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.*;
import com.as.attendance_springboot.model.enums.AuditType;
import com.as.attendance_springboot.model.enums.StaffRight;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.OffsetAttendanceServiceImpl;
import com.as.attendance_springboot.service.impl.RecordAttendanceServiceImpl;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
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
 * @description 补卡事务接口
 * @date 2023/4/19 19:29:11
 */
@Api(tags = "补卡事务接口,查询,新建,更改,删除")
@RestController
@RequestMapping("/offset-attendance")
@Slf4j
public class OffsetAttendanceController extends BaseController {
    @Autowired
    private OffsetAttendanceServiceImpl offsetAttendanceService;
    @Autowired
    private StaffServiceImpl staffService;
    @Autowired
    private RecordAttendanceServiceImpl recordAttendanceService;
    @GetMapping
    @ApiOperation("查询补卡事务,查询条件:补卡事务id,员工id,补卡记录id")
    @ApiImplicitParams({@ApiImplicitParam(name = "oaId", value = "补卡事务id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "sId", value = "员工id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "reviewerId", value = "审核人id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "rId", value = "补卡记录id", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class), @ApiResponse(code = 400,
            message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<OffsetAttendance>>> getOffsetAttendance(@RequestParam(required = false) Integer oaId,
                                                                                  @RequestParam(required = false) Integer sId,
                                                                                  @RequestParam(required = false) Integer rId,
                                                                                  @RequestParam(required = false) Integer reviewerId) {
        DataResult<List<OffsetAttendance>> result ;
        LambdaQueryWrapper<OffsetAttendance> queryWrapper = new LambdaQueryWrapper<>();
        if (oaId != null) {
            queryWrapper.eq(OffsetAttendance::getOaId, oaId);
        }
        if (sId != null) {
            queryWrapper.eq(OffsetAttendance::getSId, sId);
        }
        if (rId != null) {
            queryWrapper.eq(OffsetAttendance::getRId, rId);
        }
        if (reviewerId != null) {
            queryWrapper.eq(OffsetAttendance::getReviewer, reviewerId);
        }
        List<OffsetAttendance> list = offsetAttendanceService.list(queryWrapper);
        result = super.getModel(list);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建补卡事务")
    @ApiImplicitParam(name = "offsetAttendance", value = "OffsetAttendance类实例", dataTypeClass = OffsetAttendance.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class), @ApiResponse(code = 400,
            message = "错误的外键id", response = BaseResult.class), @ApiResponse(code = 400, message = "新建失败", response =
            BaseResult.class), @ApiResponse(code = 400, message = "补卡事务开始或结束时间不能为空", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setOffsetAttendance(@NotNull @Valid @RequestBody OffsetAttendance offsetAttendance,
                                                          BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if(isErrorForeignId(offsetAttendance)){
            result.setCode(400).setMsg("错误的外键id");
        }else if(super.isErrorTime(offsetAttendance)){
            result.setCode(400).setMsg("错误的时间");
        }else {
            result=super.setModel(offsetAttendanceService,offsetAttendance,bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping
    @ApiOperation("编辑补卡事务")
    @ApiImplicitParam(name = "offsetAttendance", value = "对应OffsetAttendance的JSON数据", dataTypeClass = String.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功"), @ApiResponse(code = 500, message = "编辑失败")})
    public ResponseEntity<BaseResult> updateOffsetAttendance(@NotNull @Valid @RequestBody OffsetAttendance offsetAttendance,
                                                             BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if(isErrorForeignId(offsetAttendance)){
            result.setCode(400).setMsg("错误的外键id");
        }else if(super.isErrorTime(offsetAttendance)){
            result.setCode(400).setMsg("错误的时间");
        }else {
            result=super.updateModelBySingle(offsetAttendance.getOaId(),offsetAttendanceService,offsetAttendance,
                                             bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @DeleteMapping
    @ApiOperation("删除未通过的补卡事务")
    @ApiImplicitParam(name = "oaId", value = "补卡事务id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功", response = BaseResult.class), @ApiResponse(code = 200,
            message = "对应事务不存在或已审核通过", response = BaseResult.class), @ApiResponse(code = 400, message = "删除失败",
            response = BaseResult.class)})
    public ResponseEntity<BaseResult> deleteOffsetAttendance(@RequestParam Integer oaId) {
        BaseResult result = new BaseResult();
        OffsetAttendance offsetAttendance=offsetAttendanceService.getById(oaId);
        if(offsetAttendance==null||offsetAttendance.getResult()== AuditType.AGREE){
            result.setCode(400).setMsg("对应事务不存在或已审核通过");
        }else {
            result=super.deleteModel(offsetAttendanceService.removeById(oaId));
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }
    private boolean isErrorForeignId(OffsetAttendance offsetAttendance){
        Staff staff=staffService.getById(offsetAttendance.getSId());
        Staff reviewer=staffService.getById(offsetAttendance.getReviewer());
        RecordAttendance recordAttendance=recordAttendanceService.getById(offsetAttendance.getRId());
        return staff==null||reviewer==null||recordAttendance==null||reviewer.getSRight()!= StaffRight.LEADER;
    }
}
