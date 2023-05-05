package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    @GET("/captcha")
    fun getCaptcha(): Call<ResponseBody>

    @POST("/login")
    fun login(@Body loginData: LoginData): Call<JwtResult<Staff>>

    @GET("/staffs/check-token")
    fun checkToken(@Header("token")token:String):Call<BaseResponse>
}