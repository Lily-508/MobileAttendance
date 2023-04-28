package com.ma.mobileattendance.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.ma.mobileattendance.MyApplication

object TokenDao {
    fun saveToken(token: String) {
        sharedPreferences().edit{
            putString("token",token)
        }
    }
    fun isTokenSaved()=sharedPreferences().contains("token")
    fun getSavedToken():String?=sharedPreferences().getString("token","")
    private fun sharedPreferences() =
        MyApplication.context.getSharedPreferences("mobile_attendance", Context.MODE_PRIVATE)
}