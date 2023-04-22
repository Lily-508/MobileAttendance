package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.RecordAttendance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object MobileAttendanceNetwork {
    private val recordAttendanceService = ServiceCreator.create(RecordAttendanceService::class.java)
    suspend fun setRecord(recordAttendance: RecordAttendance) =
        recordAttendanceService.punchAttendanceRecord(recordAttendance).await()
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation ->
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if (body!=null)continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("响应体为空"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}