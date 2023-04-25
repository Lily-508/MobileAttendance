package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.DataResponse
import com.ma.mobileattendance.logic.model.RecordAttendance
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
interface RecordAttendanceService {
    @POST("/attendances/records/punch")
    fun punchAttendanceRecord(@Body recordAttendance: RecordAttendance):Call<DataResponse<RecordAttendance>>
}