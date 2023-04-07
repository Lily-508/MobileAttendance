package com.as.attendance_springboot.security.filter;

import com.as.attendance_springboot.model.PayloadDto;
import com.as.attendance_springboot.security.token.MyAuthenticationToken;
import com.as.attendance_springboot.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT有无过滤器
 * @author xulili
 * @date 18:58 2023/4/7
 **/
//@Slf4j
//public class JwtAuthenticationFilter extends GenericFilterBean {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        String jwtToken = req.getHeader("token");
//        if (!StringUtils.hasText(jwtToken)) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//        PayloadDto payloadDto = null;
//        try {
//            payloadDto = JwtUtil.verifyTokenByHmac(jwtToken);
//        } catch (Exception e) {
////            throw new IOException("JWT验证异常: " + e.getMessage());
//            throw new AccessDeniedException("JWT认证异常:"+e.getMessage());
//        }
//        String userId=payloadDto.getUserId();
//        String username=payloadDto.getUsername();
//        log.debug("now userId:{},username:{}", userId, username);
//        MyAuthenticationToken myAuthenticationToken= new MyAuthenticationToken(userId,null,null);
//        SecurityContextHolder.getContext().setAuthentication(myAuthenticationToken);
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//}
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("token");
        if (!StringUtils.hasText(jwtToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        PayloadDto payloadDto = null;
        try {
            payloadDto = JwtUtil.verifyTokenByHmac(jwtToken);
        } catch (Exception e) {
//            throw new IOException("JWT验证异常: " + e.getMessage());
            filterChain.doFilter(request, response);
        }
        String userId=payloadDto.getUserId();
        String username=payloadDto.getUsername();
        log.info("now userId:{},username:{}", userId, username);
        MyAuthenticationToken myAuthenticationToken= new MyAuthenticationToken(userId,null,null);
        SecurityContextHolder.getContext().setAuthentication(myAuthenticationToken);
        filterChain.doFilter(request, response);
    }


}
