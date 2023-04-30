package com.as.attendance_springboot.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.as.attendance_springboot.util.RedisUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 验证码接口
 * @author xulili
 */
@RestController
@Api(tags = "验证码接口")
@Slf4j
public class CaptchaController {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取验证码图片, 验证在/login
     * @param httpServletRequest
     * @param httpServletResponse 自定义响应头uuid
     * @throws IOException
     */
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse) throws IOException {
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        String code = lineCaptcha.getCode();
        String uuid = UUID.randomUUID().toString();
        log.info("uuid={},code={}", uuid, code);
        redisUtil.set(uuid, code, 15 * 60);
        httpServletResponse.setContentType("image/png");
        httpServletResponse.setHeader("uuid", uuid);
        httpServletResponse.setStatus(200);
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        lineCaptcha.write(responseOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
