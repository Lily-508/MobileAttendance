package com.as.attendance_springboot.controller;

import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.result.DataResult;
import com.as.attendance_springboot.result.PaginationResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Objects;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description
 * @date 2023/4/12 20:03:34
 */
@Slf4j
public class BaseController {

    /**
     * 对前端参数校验结构判断
     * @param bindingResult 前端参数校验
     * @param result 响应格式
     * @author xulili
     * @date 22:08 2023/4/12
     **/
    public void validBindingResult(BindingResult bindingResult,BaseResult result) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage());
            }
            result.setCode(400).setMsg(errorMsg.toString());
            log.info("验证失败result:{}",result);
        }
    }


    /**
     * 对查询模型结果的代码重构
     * @param queryResult 数据库查询结果List或Class
     * @return com.as.attendance_springboot.result.DataResult<java.util.List < T>>
     * @author xulili
     * @date 9:49 2023/4/13
     **/
    public <T> DataResult<T> getModel(T queryResult) {
        DataResult<T> result = new DataResult<>();
        boolean success;
        if (queryResult instanceof List) {
            success = ((List<?>) queryResult).size() != 0;
        } else {
            success = (queryResult != null);
        }
        if (!success) {
            result.setCode(400).setMsg("查询失败").setData(null);
        } else {
            result.setCode(200).setMsg("查询成功").setData(queryResult);
        }
        return result;
    }


    /**
     * 对分页查询模型IPage结果的代码重构,泛型M代表实体类
     * @param modelPage 数据库分页查询
     * @return com.as.attendance_springboot.result.PaginationResult<com.baomidou.mybatisplus.core.metadata.IPage < T>>
     * @author xulili
     * @date 10:10 2023/4/13
     **/
    public <M> PaginationResult<IPage<M>> getModelPage(IPage<M> modelPage) {
        PaginationResult<IPage<M>> result = new PaginationResult<>();
        result.setCode(200).setMsg("查询成功").setData(modelPage).setTotal(modelPage.getTotal());
        return result;
    }

    /**
     * 对新建模型的代码重构,泛型M代表实体类
     * @param service ServiceImpl 对应service实例
     * @param model 对应实体类
     * @param bindingResult 前端参数校验
     * @return com.as.attendance_springboot.result.BaseResult
     * @author xulili
     * @date 22:09 2023/4/12
     **/
    public <M> BaseResult setModel(ServiceImpl<?, M> service, M model, BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        validBindingResult(bindingResult,result);
        if(result.getMsg() != null){
            return result;
        }
        if (service.save(model)) {
            result.setCode(200).setMsg("新建成功");
        } else {
            result.setCode(500).setMsg("新建失败");
        }
        return result;
    }
    /**
     * 对新建并返回模型的代码重构,泛型M代表实体类
     * @param service ServiceImpl 对应service实例
     * @param model 对应实体类
     * @param bindingResult 前端参数校验
     * @return com.as.attendance_springboot.result.BaseResult
     * @author xulili
     * @date 22:09 2023/4/12
     **/
    public <M>DataResult<M> setModelAndReturn(ServiceImpl<?, M> service, M model, BindingResult bindingResult){
        DataResult<M> result =new DataResult<>();
        validBindingResult(bindingResult,result);
        if(result.getMsg() != null){
            return result;
        }
        //调用save方法后会自动为实体类赋id值
        log.info("调用saveOrUpdate");
        try{
            if (service.saveOrUpdate(model)) {

                result.setCode(200).setMsg("新建成功").setData(model);
            } else {
                result.setCode(500).setMsg("新建失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        log.info("调用saveOrUpdate结束");
        return result;
    }
    /**
     * 对编辑单主键模型的代码重构
     * @param primaryId 表主键
     * @param service ServiceImpl 对应service实例
     * @param model 对应实体类
     * @param bindingResult 前端参数校验
     * @return com.as.attendance_springboot.result.BaseResult
     * @author xulili
     * @date 22:10 2023/4/12
     **/
    public<M> BaseResult updateModelBySingle(Integer primaryId,ServiceImpl<?, M> service, M model,
                                           BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        validBindingResult(bindingResult,result);
        if(result.getMsg() != null){
            return result;
        }
        if (primaryId == null) {
            result.setCode(400).setMsg("主键不能为null");
        } else {
            if (service.updateById(model)) {
                result.setCode(200).setMsg("编辑成功");
            } else {
                result.setCode(500).setMsg("编辑失败");
            }
        }
        return result;
    }

    /**
     * 对编辑复合主键模型的代码重构,复合主键都不为null故无需输入
     * @param mppService MppServiceImpl 对应service实例
     * @param model 对应实体类
     * @param bindingResult 前端参数校验
     * @return com.as.attendance_springboot.result.BaseResult
     * @author xulili
     * @date 22:10 2023/4/12
     **/
    public  <M> BaseResult updateModelByDouble(MppServiceImpl<?,M>mppService,M model, BindingResult bindingResult) {
        BaseResult result = new BaseResult();
        validBindingResult(bindingResult,result);
        if(result.getMsg() != null){
            return result;
        }
        if (mppService.updateByMultiId(model)) {
            result.setCode(200).setMsg("编辑成功");
        } else {
            result.setCode(500).setMsg("编辑失败");
        }
        return result;
    }

    /**
     * 对删除模型的代码重构
     * @param success 执行语句是否成功
     * @return com.as.attendance_springboot.result.BaseResult
     * @author xulili
     * @date 22:12 2023/4/12
     **/
    public BaseResult deleteModel(boolean success, String errorMsg) {
        BaseResult result = new BaseResult();
        if (success) {
            result.setCode(200).setMsg("删除成功");
        } else {
            result.setCode(500).setMsg(Objects.requireNonNullElse(errorMsg, "删除失败"));
        }
        return result;
    }
}
