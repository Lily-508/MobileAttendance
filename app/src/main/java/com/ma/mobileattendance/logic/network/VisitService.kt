package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.BaseResponse
import com.ma.mobileattendance.logic.model.DataResponse
import com.ma.mobileattendance.logic.model.Visit
import retrofit2.Call
import retrofit2.http.*

interface VisitService {
    @POST("/visit")
    fun setVisit(@Header("token") token: String,@Body visit: Visit):Call<BaseResponse>
    @GET("/visit")
    fun getVisitByDId(@Header("token") token: String,@Query("dId") dId: Int):Call<DataResponse<List<Visit>>>
    @PUT("/visit/punch-in")
    fun visitPunchIn(@Header("token") token: String,@Body visit: Visit):Call<BaseResponse>
    @PUT("/visit/punch-out")
    fun visitPunchOut(@Header("token") token: String,@Body visit: Visit):Call<BaseResponse>
}