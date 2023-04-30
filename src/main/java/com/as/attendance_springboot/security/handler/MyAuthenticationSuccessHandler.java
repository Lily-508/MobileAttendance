package com.as.attendance_springboot.security.handler;

import com.as.attendance_springboot.model.PayloadDto;
import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.result.JwtResult;
import com.as.attendance_springboot.security.token.MyAuthenticationToken;
import com.as.attendance_springboot.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        Staff userDetails = (Staff) authentication.getPrincipal();
        MyAuthenticationToken myAuthenticationToken = (MyAuthenticationToken) authentication;
        String loginPlatform=myAuthenticationToken.getLoginData().getLoginPlatform();
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
        out.write(new ObjectMapper().writeValueAsString(result));
        out.flush();
        out.close();
    }
}
