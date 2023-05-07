package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.Affair
import com.ma.mobileattendance.logic.model.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AffairService {
    @POST("/work-outside")
    fun setWorkOutside(@Header("token")token:String,@Body affair: Affair): Call<BaseResponse>
}