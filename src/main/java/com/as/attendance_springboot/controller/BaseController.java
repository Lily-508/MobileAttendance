package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.result.BaseResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description
 * @date 2023/4/12 20:03:34
 */
public class BaseController {
    /**
     * 对前端参数校验结构判断
     * @author xulili
     * @date 22:08 2023/4/12
     * @param bindingResult 前端参数校验
     * @param result 响应格式
     **/
    private void validBindingResult(BindingResult bindingResult, BaseResult result) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage());
            }
            result.setCode(400).setMsg(errorMsg.toString());
        }
    }
    /**
     * 对新建模型的代码重构
     * @author xulili
     * @date 22:09 2023/4/12
     * @param success 执行语句是否成功
     * @param bindingResult 前端参数校验
     * @return com.as.attendance_springboot.result.BaseResult
     **/
    public BaseResult setModel(boolean success, BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        validBindingResult(bindingResult, result);
        if (success) {
            result.setCode(200).setMsg("新建成功");
        } else {
            result.setCode(500).setMsg("新建失败");
        }
        return result;
    }
    /**
     * 对编辑单主键模型的代码重构
     * @author xulili
     * @date 22:10 2023/4/12
     * @param primaryId 表主键
     * @param success 执行语句是否成功
     * @param bindingResult 前端参数校验
     * @return com.as.attendance_springboot.result.BaseResult
     **/
    public BaseResult updateModelBySingle(Integer primaryId, boolean success, BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        if (primaryId == null) {
            result.setCode(400).setMsg("主键不能为null");
        } else {
            validBindingResult(bindingResult, result);
            if (success) {
                result.setCode(200).setMsg("编辑成功");
            } else {
                result.setCode(500).setMsg("编辑失败");
            }
        }
        return result;
    }
    /**
     * 对编辑复合主键模型的代码重构,复合主键都不为null故无需输入
     * @author xulili
     * @date 22:10 2023/4/12
     * @param success 执行语句是否成功
     * @param bindingResult 前端参数校验
     * @return com.as.attendance_springboot.result.BaseResult
     **/
    public BaseResult updateModelByDouble(boolean success,
                                          BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        validBindingResult(bindingResult, result);
        if (success) {
            result.setCode(200).setMsg("编辑成功");
        } else {
            result.setCode(500).setMsg("编辑失败");
        }
        return result;
    }
    /**
     * 对删除模型的代码重构
     * @author xulili
     * @date 22:12 2023/4/12
     * @param success 执行语句是否成功
     * @return com.as.attendance_springboot.result.BaseResult
     **/
    public BaseResult deleteModel(boolean success,String errorMsg){
        BaseResult result = new BaseResult();
        if(success){
            result.setCode(200).setMsg("删除成功");
        }else if(errorMsg==null){
            result.setCode(500).setMsg("删除失败");
        }else{
            result.setCode(500).setMsg(errorMsg);
        }
        return result;
    }
}
