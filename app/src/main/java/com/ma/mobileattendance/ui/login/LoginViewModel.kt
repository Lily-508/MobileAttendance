package com.ma.mobileattendance.ui.login

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.Staff
import kotlinx.coroutines.launch

class LoginViewModel :ViewModel() {
    private val _captchaLiveData=MutableLiveData<Bitmap>()
    val captchaLiveData=Transformations.switchMap(_captchaLiveData){
        Repository.getCaptcha()
    }
    fun getCaptcha(){
        _captchaLiveData.value=_captchaLiveData.value
    }
    fun insertStaff(staff: Staff){
        viewModelScope.launch {
            Repository.insertStaff(staff)
        }
    }
    fun saveTokenAndSId(token:String, sId: Int){
        Repository.saveTokenAndSId(token,sId)
    }
}