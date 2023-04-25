package com.ma.mobileattendance.logic.network

import com.ma.mobileattendance.logic.model.LoginData
import com.ma.mobileattendance.logic.model.RecordAttendance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object MobileAttendanceNetwork {
    private val recordAttendanceService = ServiceCreator.create(RecordAttendanceService::class.java)
    private val loginService=ServiceCreator.create<LoginService>()

    /**
     *获取验证码
     */
    suspend fun getCaptcha()= loginService.getCaptcha().await()
    suspend fun login(loginData: LoginData)= loginService.login(loginData).await()
    suspend fun setRecord(recordAttendance: RecordAttendance) =
        recordAttendanceService.punchAttendanceRecord(recordAttendance).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("响应体为空"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}