package com.ma.mobileattendance.ui.notice

import androidx.lifecycle.*
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.Notice

class NoticeViewModel(pageSize:Int=20): ViewModel() {
    val noticeList=ArrayList<Notice>()
    private val pageCurLiveData=MutableLiveData<Int>()
    val noticeListLiveData=Transformations.switchMap(pageCurLiveData){pageCur->
        Repository.getNoticePage(pageCur,pageSize)
    }
    fun getNextNoticeList(){
        pageCurLiveData.value=(pageCurLiveData.value?:0)+1
    }
}
class NoticeViewModelFactory(private val pageSize: Int):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoticeViewModel(pageSize) as T
    }
}