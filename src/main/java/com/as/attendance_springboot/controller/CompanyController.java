package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.Company;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.service.impl.CompanyServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
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
 * @description 公司模块接口
 * @date 2023/4/11 18:46:21
 */
@RestController
@Slf4j
@Api(tags = "公司接口,提供新建,修改,查询操作")
@RequestMapping("/companies")
public class CompanyController extends BaseController {
    @Autowired
    private CompanyServiceImpl companyService;

    @GetMapping
    @ApiOperation("查询公司,查询条件:关键词")
    @ApiImplicitParam(name = "key", value = "关键词",dataTypeClass=String.class)
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功", response = DataResult.class), @ApiResponse(code =
            400, message = "查询失败", response = DataResult.class)})
    public ResponseEntity<DataResult<List<Company>>> getCompanyByCompanyName(@RequestParam(required = false) String key) {
        key = URLDecoder.decode(key, StandardCharsets.UTF_8);
        log.info("输入关键词{}",key);
        LambdaQueryWrapper<Company>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(Company::getCName,key).or().like(Company::getCContent,key);
        List<Company>list=companyService.list(queryWrapper);
        DataResult<List<Company>> result = super.getModel(list);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PostMapping
    @ApiOperation("新建公司")
    @ApiImplicitParam(name = "company", value = "Company类实例", dataTypeClass = Company.class)
    @ApiResponses({@ApiResponse(code = 200, message = "新建成功", response = BaseResult.class), @ApiResponse(code =
            400, message = "新建失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> setCompany(@Valid @RequestBody Company company,
                                                 BindingResult bindingResult) {
        BaseResult result = super.setModel(companyService,company,bindingResult);
        return ResponseEntity.status(result.getCode()).body(result);
    }
    @PutMapping
    @ApiOperation("编辑公司")
    @ApiImplicitParam(name = "company", value = "Company类实例", dataTypeClass = Company.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功", response = BaseResult.class), @ApiResponse(code =
            400, message = "编辑失败", response = BaseResult.class)})
    public ResponseEntity<BaseResult> updateDepartment(@Valid @RequestBody Company company,
                                                       BindingResult bindingResult) {
        BaseResult result = super.updateModelBySingle(company.getCId(),companyService,company,bindingResult);
        return ResponseEntity.status(result.getCode()).body(result);
    }
    @DeleteMapping
    @ApiOperation("删除没有外键依赖公司")
    @ApiImplicitParam(name="cId",value = "公司id",dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功", response = BaseResult.class), @ApiResponse(code =
            400, message = "删除失败,有外键依赖", response = BaseResult.class)})
    public ResponseEntity<BaseResult> deleteDepartment(@RequestParam Integer cId){
        BaseResult result = new BaseResult();
        //外键依赖判断 考勤规则,外勤记录,拜访表
//        LambdaQueryWrapper<Staff> queryWrapper=new LambdaQueryWrapper<>();
        return ResponseEntity.status(result.getCode()).body(result);

    }
}
