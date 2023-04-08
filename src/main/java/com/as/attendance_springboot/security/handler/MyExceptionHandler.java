package com.as.attendance_springboot.security.handler;

import com.as.attendance_springboot.result.BaseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Spring Security异常处理 以JSON格式返回
 * @author xulili
 * @date 18:49 2023/4/7
 **/
@Component
public class MyExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private String setExceptionMsg(Integer code, String msg) throws JsonProcessingException {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(code).setMsg(msg);
        return new ObjectMapper().writeValueAsString(baseResult);
    }

    /**
     * AccessDenied 授权异常处理
     * @param request
     * @param response
     * @param accessDeniedException
     * @author xulili
     * @date 18:48 2023/4/7
     **/
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(403);
        PrintWriter out = response.getWriter();
        String errorMsg=(String)request.getAttribute("errorMsg");
        if(errorMsg != null){
            out.write(setExceptionMsg(403, "授权异常: " + errorMsg));
        }else{
            out.write(setExceptionMsg(403, "授权异常: " + accessDeniedException.getMessage()));
        }
        out.flush();
        out.close();
    }

    /**
     * AuthenticationEntryPoint 未进行认证处理
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(403);
        PrintWriter out = response.getWriter();
        String errorMsg=(String)request.getAttribute("errorMsg");
        if(errorMsg != null){
            out.write(setExceptionMsg(403, "未进行认证: " + errorMsg));
        }else{
            out.write(setExceptionMsg(403, "未进行认证: " + authException.getMessage()));
        }
        out.flush();
        out.close();
    }
}
