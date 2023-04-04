package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.result.BaseResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xulili
 * 响应/error映射,全局错误处理
 */
@RestController
@ControllerAdvice
@ApiIgnore
public class GlobalErrorController implements ErrorController {
    private static final String ERROR_PATH="/error";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResult>  handleAllExceptions(Exception e) {
        BaseResult result=new BaseResult(500,e.getMessage());
        System.out.println("设置路径出现异常");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(result);
    }

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<BaseResult> error(HttpServletResponse response){
        System.out.println("未设置路径出现异常");
        int code = response.getStatus();
        BaseResult result=null;
        switch (code) {
            case 401:
                result=new BaseResult(401,"用户未登录");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            case 403:
                result=new BaseResult(403,"没有访问权限");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
            case 404:
                result=new BaseResult(404,"请求资源不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            case 405:
                result=new BaseResult(405,"请求方法对指定资源不可用");
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
            case 500:
                result=new BaseResult(500,"服务器错误");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            default:
                result=new BaseResult(500,"未知错误");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
