package com.as.attendance_springboot.security.filter;

import com.as.attendance_springboot.model.LoginData;
import com.as.attendance_springboot.security.token.MyAuthenticationToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xulili
 */
@Slf4j
public class MyAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("进入认证入口。");
        // 校验请求方法、请求体格式
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        if (!request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            throw new AuthenticationServiceException(
                    "Authentication contentType not supported: " + request.getContentType());
        }

        // 序列化请求体,转换为LoginData
        LoginData loginData;
        ObjectMapper objectMapper = new ObjectMapper();
        // 未知字段不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            loginData = objectMapper.readValue(request.getInputStream(), LoginData.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("LoginDataJson to LoginData failed");
        }
        log.info(loginData.toString());

        // 传递令牌类 UsernamePasswordAuthenticationToken
        String username = loginData.getUsername();
        String password = loginData.getPassword();
        username = username == null ? "" : username;
        password = password == null ? "" : password;

        username = username.trim();
        MyAuthenticationToken myAuthenticationToken = new MyAuthenticationToken(username, password);
        myAuthenticationToken.setLoginData(loginData);
        return this.getAuthenticationManager().authenticate(myAuthenticationToken);
    }
}
