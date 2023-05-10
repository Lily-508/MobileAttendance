package com.as.attendance_springboot.security.handler;

import com.as.attendance_springboot.model.PayloadDto;
import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.result.JwtResult;
import com.as.attendance_springboot.security.token.MyAuthenticationToken;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
import com.as.attendance_springboot.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    public <T> T getBean(Class<T> clazz, HttpServletRequest request) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return applicationContext.getBean(clazz);
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(200);
        // 让浏览器能访问到其它响应头
        httpServletResponse.addHeader("Access-Control-Expose-Headers","token");
        PrintWriter out = httpServletResponse.getWriter();
        Staff userDetails = (Staff) authentication.getPrincipal();
        MyAuthenticationToken myAuthenticationToken = (MyAuthenticationToken) authentication;
        String loginPlatform=myAuthenticationToken.getLoginData().getLoginPlatform();
        String imei=myAuthenticationToken.getLoginData().getImei();
        if(imei!=null&&!imei.isEmpty()){
            userDetails.setSImei(imei);
            StaffServiceImpl staffService=getBean(StaffServiceImpl.class,httpServletRequest);
            staffService.updateById(userDetails);
        }
        JwtResult<Staff> result = new JwtResult<>();
        String right = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        PayloadDto payloadDto = JwtUtil.generatePayloadDto(userDetails.getSId().toString(),
                userDetails.getSName(), right,loginPlatform);
        String token;
        try {
            token = JwtUtil.generateTokenByHmac(payloadDto);
        } catch (JOSEException e) {
            throw new IOException("生成JWToken失败 "+e.getMessage());
        }
        result.setCode(200).setMsg("登陆成功").setData(userDetails).setToken(token);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        out.write(objectMapper.writeValueAsString(result));
        out.flush();
        out.close();
    }
}
