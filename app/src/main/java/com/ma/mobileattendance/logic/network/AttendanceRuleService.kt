package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.AttendanceRule
import com.ma.mobileattendance.logic.model.DataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AttendanceRuleService {
    @GET("/attendances/rules")
    fun getAttendanceRuleByAid(
        @Header("token") token: String,
        @Query("aId") aId: Int
    ): Call<DataResponse<AttendanceRule>>
}