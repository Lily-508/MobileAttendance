package com.ma.mobileattendance.ui.login

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ma.mobileattendance.logic.Repository

class LoginViewModel :ViewModel() {
    private val _captchaLiveData=MutableLiveData<Bitmap>()
    val captchaLiveData=Transformations.switchMap(_captchaLiveData){
        Repository.getCaptcha()
    }
    fun getCaptcha(){
        _captchaLiveData.value=_captchaLiveData.value
    }
}