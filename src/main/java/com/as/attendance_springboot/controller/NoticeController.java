package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.model.Notice;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.PaginationResult;
import com.as.attendance_springboot.service.impl.NoticeServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author xulili
 * Slf4j 使用日志输出log
 */
@RestController
@RequestMapping("/notices")
@Api(tags = "公告接口,提供公告新建,修改,查询,和删除操作")
@Slf4j
public class NoticeController extends BaseController{
    @Autowired
    private NoticeServiceImpl noticeService;

    @GetMapping("/page")
    @ApiOperation("分页查询所有公告")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageCur", value = "当前页数", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", dataTypeClass = Integer.class)})
    @ApiResponse(code = 200, message = "查询成功", response = PaginationResult.class)
    public ResponseEntity<PaginationResult<IPage<Notice>>> getAllNoticeByPage(@RequestParam int pageCur,
                                                                              @RequestParam int pageSize) {
        log.info("传入参数当前页数={},页面大小={}", pageCur, pageSize);
        IPage<Notice> noticeList = noticeService.page(new Page(pageCur, pageSize), null);
        long total = noticeList.getTotal();
        PaginationResult<IPage<Notice>> result = super.getModelPage(noticeList);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @ApiOperation("新建公告")
    @ApiImplicitParam(name = "notice", value = "对应Notice的JSON数据", dataTypeClass = Notice.class)
    @ApiResponses({@ApiResponse(code = 200, message = "添加Notice成功"), @ApiResponse(code = 500, message = "添加Notice失败")})
    public ResponseEntity<BaseResult> setNotice(@NotNull  @Valid @RequestBody Notice notice,
                                                BindingResult bindingResult) {
        log.info("传入notice={}", notice);
        BaseResult result = super.setModel(noticeService,notice,bindingResult);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @PutMapping
    @ApiOperation("编辑公告")
    @ApiImplicitParam(name = "notice", value = "对应Notice的JSON数据", dataTypeClass = Notice.class)
    @ApiResponses({@ApiResponse(code = 200, message = "编辑成功"), @ApiResponse(code = 500, message = "编辑失败")})
    public ResponseEntity<BaseResult> updateNotice(@NotNull @Valid @RequestBody Notice notice,
                                                   BindingResult bindingResult) {
        log.info("传入notice={}", notice);
        BaseResult result = super.updateModelBySingle(notice.getNId(),noticeService,notice,bindingResult);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @DeleteMapping
    @ApiOperation("删除公告")
    @ApiImplicitParam(name = "nId", value = "对应Notice的n_id", dataTypeClass = Integer.class)
    @ApiResponses({@ApiResponse(code = 200, message = "删除公告成功"), @ApiResponse(code = 500, message = "删除公告失败")})
    public ResponseEntity<BaseResult> deleteNoticeByNoticeId(@RequestParam int nId) {
        log.info("传入n_id={}", nId);
        BaseResult result = super.deleteModel(noticeService.removeById(nId));
        return ResponseEntity.status(result.getCode()).body(result);
    }

}


