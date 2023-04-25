package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.JwtResult
import com.ma.mobileattendance.logic.model.LoginData
import com.ma.mobileattendance.logic.model.Staff
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {
    @GET("/captcha")
    fun getCaptcha(): Call<Response>

    @POST("/login")
    fun login(@Body loginData: LoginData): Call<JwtResult<Staff>>
}