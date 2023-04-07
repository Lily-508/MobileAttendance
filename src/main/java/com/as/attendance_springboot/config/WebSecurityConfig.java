package com.as.attendance_springboot.config;

import com.as.attendance_springboot.security.filter.JwtAuthenticationFilter;
import com.as.attendance_springboot.security.filter.MyAuthenticationProcessingFilter;
import com.as.attendance_springboot.security.handler.MyAuthenticationFailureHandler;
import com.as.attendance_springboot.security.handler.MyAuthenticationSuccessHandler;
import com.as.attendance_springboot.security.handler.MyExceptionHandler;
import com.as.attendance_springboot.security.provider.MyAuthenticationTokenProvider;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author xulili
 * Spring Security自定义配置和登陆验证
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyAuthenticationTokenProvider myAuthenticationTokenProvider;
    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    MyExceptionHandler myExceptionHandler;
    /**
     * UserDetailsService实现
     * AuthenticationProvider实现
     * UsernamePasswordAuthenticationFilter 成功处理
     * UsernamePasswordAuthenticationFilter 失败处理
     */
    @Autowired
    private StaffServiceImpl staffServiceImpl;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 放行静态资源
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭 session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 添加 jwt解析
        http.addFilterBefore(new JwtAuthenticationFilter(), BasicAuthenticationFilter.class);
        // 替换原有认证入口 filter
        http.addFilterAt(myAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        // 拦截所有请求
        http.authorizeRequests()
                .antMatchers("/login","/captcha")
                .permitAll()
                .antMatchers("/**")
                .hasAnyAuthority("normal", "leader", "admin")
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable();
        http.exceptionHandling().authenticationEntryPoint(myExceptionHandler).accessDeniedHandler(myExceptionHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.userDetailsService(staffServiceImpl).passwordEncoder(encoder);
    }

    @Bean
    MyAuthenticationProcessingFilter myAuthenticationProcessingFilter() throws Exception {

        MyAuthenticationProcessingFilter filter = new MyAuthenticationProcessingFilter();
        // 认证成功的处理办法
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        // 认证错误的处理办法
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        // 把自己的authenticationManager 设置到环境中
        filter.setAuthenticationManager(new ProviderManager(myAuthenticationTokenProvider));
        filter.setFilterProcessesUrl("/login");
        return filter;
    }

}
