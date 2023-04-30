package com.ma.mobileattendance.logic.network

import android.graphics.BitmapFactory
import com.ma.mobileattendance.logic.model.Captcha
import com.ma.mobileattendance.logic.model.LoginData
import com.ma.mobileattendance.logic.model.RecordAttendance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object MobileAttendanceNetwork {
    private val recordAttendanceService = ServiceCreator.create(RecordAttendanceService::class.java)
    private val loginService=ServiceCreator.create(LoginService::class.java)

    /**
     *获取验证码
     */
    suspend fun getCaptcha():Captcha {
        return suspendCoroutine {continuation ->
            loginService.getCaptcha().enqueue(object :Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val body=response.body()
                    val uuid=response.headers().get("uuid")
                    if(response.code()==200&&body!=null&&uuid!=null){
                        val bitmap = BitmapFactory.decodeStream(body.byteStream())
                        continuation.resume(Captcha(uuid,bitmap))
                    }else{
                        continuation.resumeWithException(RuntimeException("响应异常"))
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    /**
     * 登陆
     */
    suspend fun login(loginData: LoginData)= loginService.login(loginData).await()
    suspend fun setRecord(recordAttendance: RecordAttendance) =
        recordAttendanceService.punchAttendanceRecord(recordAttendance).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (response.code()==200&&body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("响应异常"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}