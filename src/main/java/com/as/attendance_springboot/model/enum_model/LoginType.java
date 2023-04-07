package com.as.attendance_springboot.model.enum_model;

import lombok.Getter;

@Getter
public enum LoginType {
    /**登陆方式*/
    USERNAME_CODE(0,"用户名密码"),
    PHONE_CODE(1,"手机号验证码"),
    THIRD_PLATFORM(2,"第三方登陆");
    private final Integer code;
    private final String remark;
    LoginType(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }
}
