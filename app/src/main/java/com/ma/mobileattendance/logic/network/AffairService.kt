package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.Affair
import com.ma.mobileattendance.logic.model.BaseResponse
import com.ma.mobileattendance.logic.model.DataResponse
import retrofit2.Call
import retrofit2.http.*

interface AffairService {
    @POST("/work-outside")
    fun setWorkOutside(@Header("token") token: String, @Body affair: Affair): Call<BaseResponse>

    @GET("/work-outside")
    fun getWorkOutsideList(@Header("token") token: String, @Query("sId") sId: Int): Call<DataResponse<List<Affair>>>

}