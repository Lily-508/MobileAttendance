package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.VocationQuota;
import com.as.attendance_springboot.model.enums.VocationType;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
import com.as.attendance_springboot.service.impl.VocationQuotaServiceImpl;
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
 * @description 假期额度接口
 * @date 2023/4/12 19:03:36
 */
@RestController
@Slf4j
@RequestMapping("/vocation-quotas")
@Api(tags = "自定义假期借口,提供假期新建,修改,查询操作")
public class VocationQuotaController extends BaseController {
    @Autowired
    private VocationQuotaServiceImpl vocationQuotaService;
    @Autowired
    private StaffServiceImpl staffService;
    @GetMapping
    @ApiOperation("查询假期,查询条件:")
    @ApiImplicitParams({@ApiImplicitParam(name = "sId", value = "员工id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "vocationType", value = "假期类别", dataTypeClass = VocationType.class)})
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class), @ApiResponse(code = 400,
            message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<VocationQuota>>> getVocationQuota(@RequestParam Integer sId,
                                                                            @RequestParam(required = false) VocationType vocationType) {
        DataResult<List<VocationQuota>> result = new DataResult<>();
        LambdaQueryWrapper<VocationQuota> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VocationQuota::getSId, sId);
        if(vocationType!=null){
            queryWrapper.eq(VocationQuota::getVId, vocationType);
        }
        List<VocationQuota> list = vocationQuotaService.list(queryWrapper);
        if (list != null && list.size() != 0) {
            result.setCode(200).setMsg("查询成功").setData(list);
        } else {
            result.setCode(400).setMsg("查询失败").setData(null);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建假期")
    @ApiImplicitParam(name = "vocationQuota", value = "VocationQuota类实例", dataTypeClass = VocationQuota.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class), @ApiResponse(code = 400,
            message = "新建失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setVocationQuota(@Valid @RequestBody VocationQuota vocationQuota,
                                                       BindingResult bindingResult) {
        BaseResult result =new BaseResult();
        if(staffService.getById(vocationQuota.getSId())==null){
            result.setCode(400).setMsg("sId不存在");
        }else{
            result = super.setModel(vocationQuotaService.save(vocationQuota), bindingResult);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping
    @ApiOperation("编辑假期")
    @ApiImplicitParam(name = "VocationQuota", value = "VocationQuota类实例", dataTypeClass = VocationQuota.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功", response = BaseResult.class), @ApiResponse(code = 400,
            message = "编辑失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> updateVocationQuota(@Valid @RequestBody VocationQuota vocationQuota,
                                                       BindingResult bindingResult) {
        BaseResult result = super.updateModelByDouble(vocationQuotaService.updateByMultiId(vocationQuota), bindingResult);
        return ResponseEntity.status(result.getCode()).body(result);
    }

}
