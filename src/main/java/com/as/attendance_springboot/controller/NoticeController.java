package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.Notice;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.PaginationResult;
import com.as.attendance_springboot.service.NoticeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author xulili
 * Slf4j 使用日志输出log
 */
@RestController
@RequestMapping("/notices")
@Api(tags = "公告接口,提供公告新建,修改,查询,和删除操作")
@Slf4j
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/page")
    @ApiOperation("分页查询所有公告")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageCur", value = "当前页数"), @ApiImplicitParam(name = "pageSize",
            value = "页面大小")})
    @ApiResponse(code = 200, message = "查询成功")
    public ResponseEntity<PaginationResult> getAllNoticeByPage(@RequestParam int pageCur, @RequestParam int pageSize) {
        log.info("传入参数当前页数={},页面大小={}",pageCur,pageSize);
        IPage<Notice> noticeList = noticeService.page(new Page(pageCur, pageSize), null);
        long total = noticeList.getTotal();
        PaginationResult<IPage<Notice>> result = new PaginationResult<>();
        result.setCode(200);
        result.setMsg("分页查询公告成功");
        result.setData(noticeList);
        result.setTotal(total);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @ApiOperation("新建公告")
    @ApiImplicitParam(name = "notice", value = "对应Notice的JSON数据")
    @ApiResponses({@ApiResponse(code = 200, message = "添加Notice成功"), @ApiResponse(code = 500, message = "添加Notice失败")})
    public ResponseEntity<BaseResult> saveNotice(@RequestBody Notice notice) {
        log.info("传入notice={}",notice);
        boolean success = noticeService.save(notice);
        BaseResult result = new BaseResult();
        if (!success) {
            result.setCode(500).setMsg("新建公告失败");
        } else {
            result.setCode(200).setMsg("新建公告成功");
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }
    @PutMapping
    @ApiOperation("编辑公告")
    @ApiImplicitParam(name = "notice", value = "对应Notice的JSON数据")
    @ApiResponses({@ApiResponse(code = 200, message = "编辑公告成功"), @ApiResponse(code = 500, message = "编辑公告失败")})
    public ResponseEntity<BaseResult> updateNotice(@RequestBody Notice notice){
        log.info("传入notice={}",notice);
        boolean success=noticeService.updateById(notice);
        BaseResult result = new BaseResult();
        if(!success){
            result.setCode(500).setMsg("编辑公告失败");
        }else {
            result.setCode(200).setMsg("编辑公告成功");
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }
    @DeleteMapping
    @ApiOperation("删除公告")
    @ApiImplicitParam(name = "nId", value = "对应Notice的n_id")
    @ApiResponses({@ApiResponse(code = 200, message = "删除公告成功"), @ApiResponse(code = 500, message = "删除公告失败")})
    public ResponseEntity<BaseResult> deleteNotice(@RequestParam int nId){
        log.info("传入n_id={}",nId);
        boolean success=noticeService.removeById(nId);
        BaseResult result = new BaseResult();
        if(!success){
            result.setCode(500).setMsg("删除公告失败");
        }else {
            result.setCode(200).setMsg("删除公告成功");
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

}


