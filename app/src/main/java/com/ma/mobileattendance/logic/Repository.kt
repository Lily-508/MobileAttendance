package com.ma.mobileattendance.logic

import androidx.lifecycle.liveData
import com.ma.mobileattendance.logic.model.RecordAttendance
import com.ma.mobileattendance.logic.network.MobileAttendanceNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    fun setRecordAttendance(recordAttendance: RecordAttendance) = liveData(Dispatchers.IO) {
        val result = try {
            val recordAttendanceResponse = MobileAttendanceNetwork.setRecord(recordAttendance)
            if (recordAttendanceResponse.code == 200) {
                Result.success(recordAttendanceResponse.responseData)
            } else {
                Result.failure(RuntimeException("响应码异常${recordAttendanceResponse.code},异常消息${recordAttendanceResponse.msg}"))
            }
        } catch (e: Exception) {
            Result.failure<RecordAttendance>(e)
        }
        emit(result)
    }
}