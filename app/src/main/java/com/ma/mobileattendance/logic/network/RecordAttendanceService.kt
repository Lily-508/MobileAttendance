package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.DataResponse
import com.ma.mobileattendance.logic.model.RecordAttendance
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface RecordAttendanceService {
    @POST("/attendances/records/punch-in")
    fun punchIn(@Header("token") token:String, @Body recordAttendance: RecordAttendance):Call<DataResponse<RecordAttendance>>
    @PUT("/attendances/records/punch-out")
    fun punchOut(@Header("token") token:String,@Body recordAttendance: RecordAttendance):Call<DataResponse<RecordAttendance>>
}