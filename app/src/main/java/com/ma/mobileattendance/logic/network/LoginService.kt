package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.Captcha
import com.ma.mobileattendance.logic.model.JwtResult
import com.ma.mobileattendance.logic.model.LoginData
import com.ma.mobileattendance.logic.model.Staff
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {
    @GET("/captcha")
    fun getCaptcha(): Call<ResponseBody>

    @POST("/login")
    fun login(@Body loginData: LoginData): Call<JwtResult<Staff>>
}