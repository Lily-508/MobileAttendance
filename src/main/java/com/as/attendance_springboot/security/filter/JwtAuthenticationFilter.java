package com.as.attendance_springboot.security.filter;

import com.as.attendance_springboot.model.PayloadDto;
import com.as.attendance_springboot.security.token.MyAuthenticationToken;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
import com.as.attendance_springboot.util.JwtUtil;
import com.as.attendance_springboot.util.RedisUtil;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * JWT有无过滤器
 * @author xulili
 * @date 18:58 2023/4/7
 **/
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public <T> T getBean(Class<T> clazz, HttpServletRequest request) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return applicationContext.getBean(clazz);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        PayloadDto payloadDto;
        String jwtToken = request.getHeader("token");
        RedisUtil redisUtils = getBean(RedisUtil.class, request);
        StaffServiceImpl staffServiceImpl=getBean(StaffServiceImpl.class,request);
        try {
            if (!StringUtils.hasText(jwtToken)) {
                throw new JOSEException("JWT为空");
            }
            payloadDto = JwtUtil.verifyTokenByHmac(jwtToken);
            if (redisUtils.hasKey(payloadDto.getJti())) {
                throw new JOSEException("已注销的JWT,请重新登陆");
            }
            String userId = payloadDto.getUserId();
            String username = payloadDto.getUsername();
            log.info("now userId:{},username:{},JWT验证通过", userId, username);
            UserDetails userDetails=staffServiceImpl.loadUserByUsername(userId);
            log.info("根据JWT获取权限{}",userDetails.getAuthorities());
            MyAuthenticationToken myAuthenticationToken = new MyAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(myAuthenticationToken);
            filterChain.doFilter(request, response);
        } catch (JOSEException | ParseException e) {
            if(e instanceof JOSEException){
                response.setStatus(401);
                request.setAttribute("errorMsg",e.getMessage());
            }
            filterChain.doFilter(request, response);
        }
    }


}
