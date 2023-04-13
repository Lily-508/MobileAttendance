package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.AttendanceRule;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.AttendanceRuleServiceImpl;
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

    @GetMapping
    @ApiOperation("查询考勤规则,查询条件:考勤规则id,公司id")
    @ApiImplicitParams({@ApiImplicitParam(name = "aId", value = "考勤规则id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "cId", value = "公司id", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class), @ApiResponse(code =
            400, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<AttendanceRule>>> getAttendanceRuleById(@RequestParam(required = false) Integer aId,
                                                                                  @RequestParam(required = false) Integer cId) {
        log.info("输入参数{},{}", aId,cId);
        LambdaQueryWrapper<AttendanceRule> queryWrapper = new LambdaQueryWrapper<>();
        if(aId!=null){
            queryWrapper.eq(AttendanceRule::getAId,aId);
        }
        if(cId!=null){
            queryWrapper.eq(AttendanceRule::getCId,cId);
        }
        List<AttendanceRule> list = attendanceRuleService.list(queryWrapper);
        DataResult<List<AttendanceRule>> result = super.getModel(list);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建考勤规则")
    @ApiImplicitParam(name = "attendanceRule", value = "AttendanceRule类实例", dataTypeClass = AttendanceRule.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class), @ApiResponse(code =
            400, message = "新建失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setAttendanceRule(@Valid @RequestBody AttendanceRule attendanceRule,
                                                        BindingResult bindingResult) {
        BaseResult result = super.setModel(attendanceRuleService,attendanceRule, bindingResult);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping
    @ApiOperation("编辑考勤规则")
    @ApiImplicitParam(name = "attendanceRule", value = "AttendanceRule类实例", dataTypeClass = AttendanceRule.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功", response = BaseResult.class), @ApiResponse(code =
            400, message = "编辑失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> updateDepartment(@Valid @RequestBody AttendanceRule attendanceRule,
                                                       BindingResult bindingResult) {
        BaseResult result = super.updateModelBySingle(attendanceRule.getAId(),
                attendanceRuleService,attendanceRule, bindingResult);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @DeleteMapping
    @ApiOperation("删除没有外键依赖考勤规则")
    @ApiImplicitParam(name = "aId", value = "考勤规则id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功", response = BaseResult.class), @ApiResponse(code =
            400, message = "删除失败,有外键依赖", response = BaseResult.class)})
    public ResponseEntity<BaseResult> deleteDepartment(@RequestParam Integer aId) {
        //外键依赖判断 CASCADE依赖公司表 直接删除
        BaseResult result = super.deleteModel(attendanceRuleService.removeById(aId), "删除失败,有外键依赖");
        return ResponseEntity.status(result.getCode()).body(result);
    }

}
