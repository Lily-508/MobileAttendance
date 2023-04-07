package com.as.attendance_springboot.model;

import lombok.Data;

@Data
public class LoginData {
    /** 验证码对应标识 */
    private String uuid;
    /** 登陆方式 */
    private String loginType;
    private String username;
    private String password;
    private String phone;
    /**验证码*/
    private String code;
    /** 第三方平台类型 */
    private String thirdPlatformType;
    /** 第三方平台id */
    private String thirdPlatformId;
}
