package com.ma.mobileattendance.logic

import android.graphics.BitmapFactory
import androidx.lifecycle.liveData
import com.ma.mobileattendance.logic.model.Captcha
import com.ma.mobileattendance.logic.model.LoginData
import com.ma.mobileattendance.logic.model.RecordAttendance
import com.ma.mobileattendance.logic.network.MobileAttendanceNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    /**
     * 获取验证码
     */
    fun getCaptcha() = liveData(Dispatchers.IO) {
        val result = try {
            val response = MobileAttendanceNetwork.getCaptcha()
            val body = response.body()
            val uuid = response.headers().get("uuid")
            if (body != null && uuid != null) {
                val bitmap = BitmapFactory.decodeStream(body.byteStream())
                Result.success(Captcha(uuid, bitmap))
            } else {
                Result.failure(ExceptionInInitializerError("验证码响应有空值"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    /**
     * 用户登录
     */
    fun login(loginData:LoginData)=liveData(Dispatchers.IO){
        val result=try{
            val loginResponse=MobileAttendanceNetwork.login(loginData)
            if(loginResponse.code==200){
                Result.success(loginResponse)
            }else{
                Result.failure(RuntimeException("响应码异常${loginResponse.code},异常消息${loginResponse.msg}"))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun setRecordAttendance(recordAttendance: RecordAttendance) = liveData(Dispatchers.IO) {
        val result = try {
            val recordAttendanceResponse = MobileAttendanceNetwork.setRecord(recordAttendance)
            if (recordAttendanceResponse.code == 200) {
                Result.success(recordAttendanceResponse.responseData)
            } else {
                Result.failure(RuntimeException("响应码异常${recordAttendanceResponse.code},异常消息${recordAttendanceResponse.msg}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
}