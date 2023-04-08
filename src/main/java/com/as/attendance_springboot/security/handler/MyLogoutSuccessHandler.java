package com.as.attendance_springboot.security.handler;

import com.as.attendance_springboot.model.PayloadDto;
import com.as.attendance_springboot.result.BaseResult;
import com.as.attendance_springboot.util.JwtUtil;
import com.as.attendance_springboot.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description spring security成功登出JSON响应
 * @date 2023/4/8 14:37:11
 */
@Component
@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    RedisUtil redisUtil;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        BaseResult baseResult=new BaseResult();
        baseResult.setCode(200).setMsg("登出成功");
        String token=request.getHeader("token");
        try {
            PayloadDto payloadDto= JwtUtil.getByToken(token);
            redisUtil.set(payloadDto.getJti(), "已注销JWT",5*60*60);
            log.info("登出成功 {}",authentication);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper().writeValueAsString(baseResult));
            out.flush();
            out.close();
        } catch (ParseException e) {
            log.error("token转化为PayloadDto失败");
//            throw new IOException(e);
        }
    }
}
