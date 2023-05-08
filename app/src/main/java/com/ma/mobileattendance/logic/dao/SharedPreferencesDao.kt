package com.ma.mobileattendance.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.ma.mobileattendance.MyApplication
import com.ma.mobileattendance.logic.model.RecordAttendance

object SharedPreferencesDao {

    fun saveRecordAttendance(recordAttendance: RecordAttendance){
        sharedPreferences().edit {
            putString("recordAttendance",Gson().toJson(recordAttendance))
        }
    }
    fun removeRecordAttendance(){
        sharedPreferences().edit {
            remove("recordAttendance")
        }
    }
    fun getRecordAttendance():RecordAttendance?{
        val recordAttendance= sharedPreferences().getString("recordAttendance","")
        return Gson().fromJson(recordAttendance,RecordAttendance::class.java)
    }

    fun saveTokenAndSId(token: String, sId: Int) {
        sharedPreferences().edit {
            putString("token", token)
            putInt("sId", sId)
        }

    }
    fun removeTokenAndSId(){
        sharedPreferences().edit {
            remove("token")
            remove("sId")
        }
    }
    fun isSIdSaved()= sharedPreferences().contains("sId")
    fun isTokenSaved() = sharedPreferences().contains("token")

    fun getSavedToken(): String? = sharedPreferences().getString("token", "")
    fun getSavedSId(): Int = sharedPreferences().getInt("sId",0)
    private fun sharedPreferences() =
        MyApplication.context.getSharedPreferences("mobile_attendance", Context.MODE_PRIVATE)
}