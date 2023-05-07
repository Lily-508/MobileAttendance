package com.ma.mobileattendance.ui.affair.workoutside

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.Affair

class WorkOutsidePunchViewModel : ViewModel() {
    val workOutsideList = ArrayList<Affair>()
    private val _sIdLiveData = MutableLiveData<Int>()
    val sIdLiveData = Transformations.switchMap(_sIdLiveData) { sId ->
        Repository.getWorkOutsideList(sId)
    }

    fun getWorkOutsideList(sId: Int) {
        _sIdLiveData.value = sId
    }
}