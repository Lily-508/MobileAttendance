package com.ma.mobileattendance.logic

import android.graphics.BitmapFactory
import androidx.lifecycle.liveData
import com.ma.mobileattendance.MyApplication
import com.ma.mobileattendance.logic.dao.TokenDao
import com.ma.mobileattendance.logic.model.Captcha
import com.ma.mobileattendance.logic.model.LoginData
import com.ma.mobileattendance.logic.model.RecordAttendance
import com.ma.mobileattendance.logic.model.Staff
import com.ma.mobileattendance.logic.network.MobileAttendanceNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    private val appDatabase: AppDatabase = AppDatabase.getDatabase(MyApplication.context)

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
    fun login(loginData: LoginData) = liveData(Dispatchers.IO) {
        val result = try {
            val loginResponse = MobileAttendanceNetwork.login(loginData)
            if (loginResponse.code == 200) {
                Result.success(loginResponse)
            } else {
                Result.failure(RuntimeException("响应码异常${loginResponse.code},异常消息${loginResponse.msg}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    /**
     * 用户数据存储
     */
    fun insertStaff(staff: Staff) = appDatabase.staffDao().insertStaff(staff)
    fun selectStaffBySId(sId: Int) = appDatabase.staffDao().selectBySId(sId)
    fun updateStaff(staff: Staff)= appDatabase.staffDao().updateStaff(staff)
    fun deleteStaff(staff: Staff)= appDatabase.staffDao().deleteStaff(staff)

    /**
     * sharedPreferences存储token相关
     */
    fun saveToken(token: String) = TokenDao.saveToken(token)

    fun isTokenSaved() = TokenDao.isTokenSaved()

    fun getToken() = TokenDao.getSavedToken()

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