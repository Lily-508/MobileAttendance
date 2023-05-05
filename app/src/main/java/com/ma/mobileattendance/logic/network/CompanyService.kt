package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.Company
import com.ma.mobileattendance.logic.model.DataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CompanyService {
    @GET("/companies")
    fun getCompanyByCId(@Header("token") token: String, @Query("cId") cId: Int): Call<DataResponse<Company>>
}