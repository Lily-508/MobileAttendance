package com.as.attendance_springboot.security.provider;

import com.as.attendance_springboot.model.LoginData;
import com.as.attendance_springboot.model.enums.LoginType;
import com.as.attendance_springboot.security.token.MyAuthenticationToken;
import com.as.attendance_springboot.service.impl.StaffServiceImpl;
import com.as.attendance_springboot.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author xulili
 */
@Component
@Slf4j
public class MyAuthenticationTokenProvider implements AuthenticationProvider {
    @Autowired
    StaffServiceImpl myUserDetailService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 认证逻辑
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyAuthenticationToken myAuthenticationToken = (MyAuthenticationToken) authentication;
        LoginData loginData = myAuthenticationToken.getLoginData();
        log.info("MyAuthenticationTokenProvider开始认证{}", loginData);
        if (loginData == null) {
            throw new AuthenticationServiceException("未获取到登陆参数");
        }
        Integer loginType = loginData.getLoginType();
        UserDetails userDetails;
        if (loginType == null || loginData.getLoginPlatform() == null ||
                 "".equals(loginData.getLoginPlatform())) {
            throw new AuthenticationServiceException("登陆方式或平台不可为空");
        }
        if (LoginType.USERNAME_CODE.getCode().equals(loginType)) {
            // 用户名密码登陆
            log.info("尝试以 {} 方式登陆", LoginType.USERNAME_CODE.getRemark());
            this.checkUsernameCode(loginData.getUuid(), loginData.getCode());
            userDetails = myUserDetailService.loadUserByUsername(loginData.getUsername());
        } else if (LoginType.PHONE_CODE.getCode().equals(loginType)) {
            // 手机号验证码登陆
            log.info("尝试以 {} 方式登陆", LoginType.PHONE_CODE.getRemark());
//            this.checkPhoneCode(loginData.getPhone(),loginData.getPhoneVerifyCode());
            userDetails = myUserDetailService.loadUserByPhone(loginData.getPhone());
        } else if (LoginType.THIRD_PLATFORM.getCode().equals(loginType)) {
            // 三方平台登陆
            log.info("尝试以 {} 方式登陆", LoginType.THIRD_PLATFORM.getRemark());
            userDetails = myUserDetailService.loadByThirdPlatformId(loginData.getThirdPlatformType(),
                                                                    loginData.getThirdPlatformId());
        } else {
            throw new AuthenticationServiceException("不支持的登陆方式");
        }

        // 认证成功
        MyAuthenticationToken token = new MyAuthenticationToken(userDetails, loginData.getPassword(),
                                                                userDetails.getAuthorities());
        token.setLoginData(loginData);
        token.setDetails(userDetails);
        return token;
    }


    /**
     * 修改此 provider 支持自定义的令牌类
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (MyAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public void checkPhoneCode(String phone, String code) {
        // todo 校验手机验证码 示例
//        if ("111111".equals(code)) {
//        }else {
//            throw new AuthenticationServiceException("手机验证码错误");
//        }
    }

    /**
     * 校验图片登陆验证码
     * @param uuid
     * @param code
     * @author xulili
     * @date 13:56 2023/4/8
     **/
    public void checkUsernameCode(String uuid, String code) {
        if(uuid==null||code==null){
            throw new AuthenticationServiceException("请重新获取验证码");
        }else {
            String redisCode = (String) redisUtil.get(uuid);
            log.info(redisCode);
            if (redisCode == null || !redisCode.equalsIgnoreCase(code)) {
                throw new AuthenticationServiceException("验证码为空或验证码错误");
            }
            //确保验证码的一次性
            redisUtil.remove(uuid);
        }
    }
}
