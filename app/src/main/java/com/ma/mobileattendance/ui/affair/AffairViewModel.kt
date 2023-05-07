package com.ma.mobileattendance.ui.affair

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AffairViewModel : ViewModel() {
    private val _totalLiveDate = MutableLiveData<Long>()
    val totalLiveData: LiveData<Long> get() = _totalLiveDate
    fun computeTotal(start: String, end: String) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val startDate = sdf.parse(start)
            val endDate = sdf.parse(end)
            _totalLiveDate.value = (endDate.time - startDate.time) / 60
            Log.d("AffairActivity", "total:${_totalLiveDate.value}")
        } catch (e: ParseException) {
            _totalLiveDate.value = -1
            Log.d("AffairActivity", "ParseException,start:$start,end:$end")
        }
    }
}