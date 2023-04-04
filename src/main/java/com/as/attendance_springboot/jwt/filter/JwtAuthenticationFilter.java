//package com.as.attendance_springboot.jwt.filter;
//
//import com.as.attendance_springboot.jwt.dto.PayloadDto;
//import com.as.attendance_springboot.jwt.util.JwtUtil;
//import com.nimbusds.jose.JOSEException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.text.ParseException;
//
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String token=request.getHeader("Authorization");
//        if(token==null){
//            filterChain.doFilter(request,response);
//            return;
//        }
//        try {
//            SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
//        }
//    }
//
//    private UsernamePasswordAuthenticationToken getAuthentication(String token)throws ParseException, JOSEException {
//        PayloadDto payloadDto= JwtUtil.verifyTokenByHmac(token);
//        String userid=payloadDto.getUserid();
//
//
//    }
//}
