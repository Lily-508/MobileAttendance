package com.ma.mobileattendance.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.Staff
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    var staff:Staff?=null
    private val _staffLiveData= MutableLiveData<Staff>()
    val staffLiveData= Transformations.switchMap(_staffLiveData){
        Repository.updateStaff(it)
    }
    fun updateStaff(){
        if(staff!=null){
            _staffLiveData.value=staff
            viewModelScope.launch{
                Repository.updateStaffByRoom(staff!!)
            }
        }else{
            Log.d("UserActivity","viewmodel中staff为null")
        }
    }
}