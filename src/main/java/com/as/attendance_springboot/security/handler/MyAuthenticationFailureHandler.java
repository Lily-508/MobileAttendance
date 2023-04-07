package com.as.attendance_springboot.security.handler;

import com.as.attendance_springboot.result.BaseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * @author xulili
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        BaseResult result=new BaseResult();
        result.setCode(400);
        if (exception instanceof LockedException) {
            result.setMsg("账户已锁定!");
        } else if (exception instanceof DisabledException) {
            result.setMsg("账户已禁用!");
        } else if (exception instanceof BadCredentialsException) {
            result.setMsg("用户名或者密码输入错误，请重新输入!");
        } else if (exception instanceof AuthenticationServiceException) {
            result.setMsg("AuthenticationServiceException错误: "+exception.getMessage());
        }else{
            result.setMsg("错误信息: "+exception.getMessage());
        }
        out.write(new ObjectMapper().writeValueAsString(result));
        out.flush();
        out.close();
    }
}
