package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.Notice
import com.ma.mobileattendance.logic.model.PageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NoticeService {
    @GET("/notices/page")
    fun getNoticePage(
        @Header("token") token: String,
        @Query("pageCur") pageCur: Int,
        @Query("pageSize") pageSize: Int
    ): Call<PageResponse<Notice>>
}