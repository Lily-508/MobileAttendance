package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.BaseResponse
import com.ma.mobileattendance.logic.model.Staff
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

interface StaffService {
    @PUT("/staffs")
    fun updateStaff(@Header("token")token:String,@Body staff:Staff):Call<BaseResponse>
}