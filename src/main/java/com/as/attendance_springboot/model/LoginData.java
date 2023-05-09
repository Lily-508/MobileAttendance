package com.as.attendance_springboot.model;

import lombok.Data;

/**
 * @author xulili
 */
@Data
public class LoginData {
    /** 验证码对应标识 */
    private String uuid;
    /** 登陆方式 */
    private Integer loginType;
    /**登陆平台**/
    private String loginPlatform;
    private String username;
    private String password;
    private String phone;
    private String imei;
    /**验证码*/
    private String code;
    /** 第三方平台类型 */
    private String thirdPlatformType;
    /** 第三方平台id */
    private String thirdPlatformId;
}
