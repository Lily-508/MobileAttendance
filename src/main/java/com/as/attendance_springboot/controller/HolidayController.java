package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.Holiday;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.HolidayServiceImpl;
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
 * @description 自定义假期接口
 * @date 2023/4/12 14:01:38
 */
@RestController
@Slf4j
@RequestMapping("/holidays")
@Api(tags = "自定义假期接口,提供假期新建,修改,查询,和删除操作")
public class HolidayController extends BaseController{
    @Autowired
    private HolidayServiceImpl holidayService;
    @GetMapping
    @ApiOperation("查询假期,查询条件:年份")
    @ApiImplicitParam(name = "hYear", value = "年份名",dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class),
            @ApiResponse(code = 500, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<Holiday>>> getHolidayByHolidayYear(@RequestParam(required = false) Integer hYear) {
        LambdaQueryWrapper<Holiday>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(Holiday::getHYear,hYear);
        List<Holiday>list=holidayService.list(queryWrapper);
        DataResult<List<Holiday>> result = super.getModel(list);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建假期")
    @ApiImplicitParam(name = "holiday", value = "Holiday类实例", dataTypeClass = Holiday.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class),
            @ApiResponse(code = 500, message = "新建失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setHoliday(@Valid @RequestBody Holiday holiday,
                                                 BindingResult bindingResult) {
        BaseResult result = super.setModel(holidayService,holiday,bindingResult);
        return ResponseEntity.status(result.getCode()).body(result);
    }
    @PutMapping
    @ApiOperation("编辑假期")
    @ApiImplicitParam(name = "Holiday", value = "Holiday类实例", dataTypeClass = Holiday.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功", response = BaseResult.class),
            @ApiResponse(code = 500, message = "编辑失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> updateDepartment(@Valid @RequestBody Holiday holiday,
                                                       BindingResult bindingResult) {
        BaseResult result = super.updateModelBySingle(holiday.getHId(),holidayService,holiday,bindingResult);
        return ResponseEntity.status(result.getCode()).body(result);
    }
    @DeleteMapping
    @ApiOperation("删除假期")
    @ApiImplicitParam(name="hYear",value = "年份名",dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功", response = BaseResult.class),
            @ApiResponse(code = 500, message = "删除失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> deleteDepartment(@RequestParam Integer hId){
        BaseResult result = super.deleteModel(holidayService.removeById(hId));
        return ResponseEntity.status(result.getCode()).body(result);
    }

}
