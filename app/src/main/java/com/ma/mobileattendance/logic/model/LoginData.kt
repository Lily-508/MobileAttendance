package com.ma.mobileattendance.logic.model

data class LoginData(
    /** 验证码对应标识  */
    val uuid: String,
    /** 登陆方式  */
    val loginType: Int,
    val username: String,
    /**验证码 */
    val code: String,
    val password: String
) {
    /**登陆平台 */
    var loginPlatform: String = "android"
    var phone: String? = null
    /** 第三方平台类型  */
    var thirdPlatformType: String? = null
    /** 第三方平台id  */
    var thirdPlatformId: String? = null
}