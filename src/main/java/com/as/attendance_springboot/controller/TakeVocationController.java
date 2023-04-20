package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.*;
import com.as.attendance_springboot.model.enums.AuditType;
import com.as.attendance_springboot.model.enums.StaffRight;
import com.as.attendance_springboot.model.enums.VocationType;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
import com.as.attendance_springboot.service.impl.TakeVocationServiceImpl;
import com.as.attendance_springboot.service.impl.VocationQuotaServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 请休假事务接口
 * @date 2023/4/19 19:25:22
 */
@Api(tags = "请休假事务接口,查询,新建,更改,删除")
@RestController
@RequestMapping("/take-vocation")
@Slf4j
public class TakeVocationController extends BaseController{
    @Autowired
    private TakeVocationServiceImpl takeVocationService;
    @Autowired
    private StaffServiceImpl staffService;
    @Autowired
    private VocationQuotaServiceImpl vocationQuotaService;
    @GetMapping
    @ApiOperation("查询请休假事务,查询条件:请休假事务id,员工id,公司id")
    @ApiImplicitParams({@ApiImplicitParam(name = "oaId", value = "请休假事务id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "sId", value = "员工id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "reviewerId", value = "审核人id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "vocationType", value = "假期类别", dataTypeClass = Integer.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class),
            @ApiResponse(code = 400, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<TakeVocation>>> getTakeVocation(@RequestParam(required = false)Integer tvId,
                                                                          @RequestParam(required = false) Integer sId,
                                                                          @RequestParam(required = false) String vocationType,
                                                                          @RequestParam(required = false) Integer reviewerId) {
        DataResult<List<TakeVocation>> result;
        vocationType= URLDecoder.decode(vocationType, StandardCharsets.UTF_8);
        VocationType vId= VocationType.fromText(vocationType);
        LambdaQueryWrapper<TakeVocation> queryWrapper = new LambdaQueryWrapper<>();
        if (tvId != null) {
            queryWrapper.eq(TakeVocation::getVId, tvId);
        }
        if (sId != null) {
            queryWrapper.eq(TakeVocation::getSId, sId);
        }
        if (vId != null) {
            queryWrapper.eq(TakeVocation::getVId, vId);
        }
        if (reviewerId != null) {
            queryWrapper.eq(TakeVocation::getReviewer, reviewerId);
        }
        List<TakeVocation> list = takeVocationService.list(queryWrapper);
        result = super.getModel(list);
        return ResponseEntity.status(result.getCode()).body(result);
    }
    @PostMapping
    @ApiOperation("新建请休假事务")
    @ApiImplicitParam(name = "TakeVocation", value = "TakeVocation类实例", dataTypeClass = TakeVocation.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class),
            @ApiResponse(code = 400, message = "错误的外键id", response = BaseResult.class),
            @ApiResponse(code = 400, message = "新建失败", response = BaseResult.class),
            @ApiResponse(code = 400, message = "请休假事务开始或结束时间不能为空", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setTakeVocation(@NotNull @Valid @RequestBody TakeVocation takeVocation,
                                                     BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if(isErrorForeignId(takeVocation)){
            result.setCode(400).setMsg("错误的外键id");
        }else if(super.isErrorTime(takeVocation)){
            result.setCode(400).setMsg("错误的时间");
        }else {
            result=super.setModel(takeVocationService,takeVocation,bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }
    @PutMapping
    @ApiOperation("编辑请休假事务")
    @ApiImplicitParam(name = "takeVocation", value = "对应TakeVocation的JSON数据", dataTypeClass = String.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功"),
            @ApiResponse(code = 500, message = "编辑失败")})
    public ResponseEntity<BaseResult> updateTakeVocation(@NotNull @Valid @RequestBody TakeVocation takeVocation,
                                                        BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if(isErrorForeignId(takeVocation)){
            result.setCode(400).setMsg("错误的外键id");
        }else if(super.isErrorTime(takeVocation)){
            result.setCode(400).setMsg("错误的时间");
        }else {
            result=super.updateModelBySingle(takeVocation.getTkId(),takeVocationService,takeVocation,
                                             bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }
    @DeleteMapping
    @ApiOperation("删除未通过的请休假事务")
    @ApiImplicitParam(name = "tvId", value = "请休假事务id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功", response = BaseResult.class),
            @ApiResponse(code = 200, message = "对应事务不存在或已审核通过", response = BaseResult.class),
            @ApiResponse(code = 400, message = "删除失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> deleteTakeVocation(@RequestParam Integer tvId) {
        //外键依赖判断 没有被用作外键直接删除
        BaseResult result = new BaseResult();
        TakeVocation takeVocation=takeVocationService.getById(tvId);
        if(takeVocation==null||takeVocation.getResult()== AuditType.AGREE){
            result.setCode(400).setMsg("对应事务不存在或已审核通过");
        }else {
            result=super.deleteModel(takeVocationService.removeById(tvId));
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }
    private boolean isErrorForeignId(TakeVocation takeVocation){
        Staff staff=staffService.getById(takeVocation.getSId());
        Staff reviewer=staffService.getById(takeVocation.getReviewer());
        LambdaQueryWrapper<VocationQuota>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(VocationQuota::getVId,takeVocation.getVId())
                          .eq(VocationQuota::getSId,takeVocation.getSId());
        VocationQuota vocationQuota=vocationQuotaService.getOne(lambdaQueryWrapper);
        return staff==null||reviewer==null||vocationQuota==null||reviewer.getSRight()!= StaffRight.LEADER;
    }
}
