package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.Visit;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.CompanyServiceImpl;
import com.as.attendance_springboot.service.impl.DepartmentServiceImpl;
import com.as.attendance_springboot.service.impl.VisitServiceImpl;
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
 * @description 拜访表Controller层
 * @date 2023/4/18 09:35:57
 */
@RestController
@RequestMapping("/visit")
@Api(tags = "拜访表接口,新建,修改,查询,删除")
@Slf4j
public class VisitController extends BaseController {
    @Autowired
    private VisitServiceImpl visitService;
    @Autowired
    private CompanyServiceImpl companyService;
    @Autowired
    private DepartmentServiceImpl departmentService;

    @GetMapping
    @ApiOperation("查询拜访表,查询条件:拜访表id,公司id,部门id")
    @ApiImplicitParams({@ApiImplicitParam(name = "vId", value = "拜访表id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "cId", value = "公司id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "dId", value = "部门id", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class),
            @ApiResponse(code = 500, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<Visit>>> getVisitById(@RequestParam(required = false) Integer vId,
                                                                @RequestParam(required = false) Integer cId,
                                                                @RequestParam(required = false) Integer dId) {
        LambdaQueryWrapper<Visit> queryWrapper = new LambdaQueryWrapper<>();
        if (vId != null) {
            queryWrapper.eq(Visit::getVId, vId);
        }
        if (cId != null) {
            queryWrapper.eq(Visit::getCId, cId);
        }
        if (dId != null) {
            queryWrapper.eq(Visit::getDId, dId);
        }
        List<Visit> list = visitService.list(queryWrapper);
        DataResult<List<Visit>> result = super.getModel(list);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建拜访计划")
    @ApiImplicitParam(name = "visit", value = "Visit类实例", dataTypeClass = Visit.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class),
            @ApiResponse(code = 400, message = "错误的外键id", response = BaseResult.class),
            @ApiResponse(code = 500, message = "新建失败", response = BaseResult.class),
            @ApiResponse(code = 400, message = "拜访计划开始或结束时间不能为空", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setVisit(@NotNull @Valid @RequestBody Visit visit,
                                               BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (isErrorForeignId(visit)) {
            log.info("VisitController外键有效性判断:无效,公司id={},部门id={}", visit.getCId(), visit.getDId());
            result.setCode(400).setMsg("错误的外键id");
        } else if (visit.getVStart() == null || visit.getVEnd() == null) {
            result.setCode(400).setMsg("拜访计划开始或结束时间不能为空");
        } else {
            result = super.setModel(visitService, visit, bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping("/punch-in")
    @ApiOperation("拜访签到")
    @ApiImplicitParam(name = "visit", value = "Visit类实例", dataTypeClass = Visit.class)
    @ApiResponses({@ApiResponse(code = 200, message = "签到成功", response = BaseResult.class),
            @ApiResponse(code = 500, message = "签到失败", response = BaseResult.class),
            @ApiResponse(code = 400, message = "错误的外键id", response = BaseResult.class),
            @ApiResponse(code = 400, message = "签到参数格式错误", response = BaseResult.class),
            @ApiResponse(code = 400, message = "签到操作只能进行一次", response = BaseResult.class),
            @ApiResponse(code = 400, message = "拜访计划不存在", response = BaseResult.class)})
    public ResponseEntity<BaseResult> visitPunchIn(@NotNull @Valid @RequestBody Visit visit,
                                                   BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (!super.validBindingResult(bindingResult, result)) {
            return ResponseEntity.status(result.getCode()).body(result);
        } else if (isErrorForeignId(visit)) {
            log.info("外键有效性判断:无效,公司id={},部门id={}", visit.getCId(), visit.getDId());
            result.setCode(400).setMsg("错误的外键id");
        } else if (visit.getVPunchIn() == null || visit.getPunchInPlace() == null || visit.getVId() == null) {
            result.setCode(400).setMsg("签到参数格式错误");
        } else {
            Visit v = visitService.getById(visit.getVId());
            if (v == null) {
                result.setCode(400).setMsg("拜访计划不存在");
            } else if (v.getPunchInPlace() != null) {
                result.setCode(400).setMsg("签到操作只能进行一次");
            } else if (visitService.updateById(visit)) {
                result.setCode(200).setMsg("签到成功");
            } else {
                result.setCode(500).setMsg("签到失败");
            }
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping("/punch-out")
    @ApiOperation("拜访签退")
    @ApiImplicitParam(name = "visit", value = "Visit类实例", dataTypeClass = Visit.class)
    @ApiResponses({@ApiResponse(code = 200, message = "签退成功", response = BaseResult.class),
            @ApiResponse(code = 500, message = "签退失败", response = BaseResult.class),
            @ApiResponse(code = 400, message = "错误的外键id", response = BaseResult.class),
            @ApiResponse(code = 400, message = "签退参数格式错误", response = BaseResult.class),
            @ApiResponse(code = 400, message = "签退操作只能进行一次", response = BaseResult.class),
            @ApiResponse(code = 400, message = "拜访计划不存在", response = BaseResult.class),
            @ApiResponse(code = 400, message = "请先签到", response = BaseResult.class)})
    public ResponseEntity<BaseResult> visitPunchOut(@NotNull @Valid @RequestBody Visit visit,
                                                    BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (!super.validBindingResult(bindingResult, result)) {
            return ResponseEntity.status(result.getCode()).body(result);
        } else if (isErrorForeignId(visit)) {
            log.info("外键有效性判断:无效,公司id={},部门id={}", visit.getCId(), visit.getDId());
            result.setCode(400).setMsg("错误的外键id");
        } else if (visit.getVPunchOut() == null || visit.getPunchOutPlace() == null || visit.getVId() == null) {
            result.setCode(400).setMsg("签退参数格式错误");
        } else {
            Visit v = visitService.getById(visit.getVId());
            if (v == null) {
                result.setCode(400).setMsg("拜访计划不存在");
            } else if (v.getVPunchIn() == null || v.getPunchInPlace() == null) {
                result.setCode(400).setMsg("请先签到");
            } else if (v.getVPunchOut() != null) {
                result.setCode(400).setMsg("签退操作只能进行一次");
            } else if (visitService.updateById(visit)) {
                result.setCode(200).setMsg("签退成功");
            } else {
                result.setCode(500).setMsg("签退失败");
            }
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @DeleteMapping
    @ApiOperation("删除拜访表")
    @ApiImplicitParam(name = "vId", value = "拜访表id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功", response = BaseResult.class),
            @ApiResponse(code = 500, message = "删除失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> deleteDepartment(@RequestParam Integer vId) {
        //外键依赖判断 没有被用作外键直接删除
        BaseResult result = super.deleteModel(visitService.removeById(vId));
        return ResponseEntity.status(result.getCode()).body(result);
    }

    private boolean isErrorForeignId(Visit visit) {
        return visit.getCId() == null || visit.getDId() == null || departmentService.getById(visit.getDId()) == null ||
                companyService.getById(visit.getCId()) == null;
    }

}
