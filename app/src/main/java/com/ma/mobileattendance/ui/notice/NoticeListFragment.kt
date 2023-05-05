package com.ma.mobileattendance.ui.notice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.FragmentNormalAttendanceBinding
import com.ma.mobileattendance.databinding.FragmentNoticeListBinding
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.EnumNoun
import com.ma.mobileattendance.logic.model.ErrorResponseException
import com.ma.mobileattendance.logic.model.Notice

class NoticeListFragment : Fragment() {
    private  var _binding:FragmentNoticeListBinding?=null
    private lateinit var adapter: NoticeListAdapter
    private val binding get() = _binding!!
    private var pageSize=20
    private val viewModel by lazy { ViewModelProviders.of(this,NoticeViewModelFactory(pageSize)).get(NoticeViewModel::class.java) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter=NoticeListAdapter(viewModel.noticeList)
        binding.noticeListRecycleView.adapter=adapter
        binding.noticeListRecycleView.layoutManager=LinearLayoutManager(context)
        viewModel.getNextNoticeList()
        viewModel.noticeListLiveData.observe(viewLifecycleOwner){result->
            val noticeList=result.getOrNull()
            Log.d("NoticeActivity","响应体$result")
            if(noticeList!=null){
                viewModel.noticeList.addAll(noticeList)
                adapter.notifyDataSetChanged()
            }else{
                val exception = result.exceptionOrNull()
                if (exception is ErrorResponseException) {
                    val response = exception.getResponse()
                    Toast.makeText(activity, response.msg, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(activity, "获取公告失败,请重试", Toast.LENGTH_LONG).show()
                    Log.d("ActivityPunch", "获取公告失败$exception")
                    exception?.printStackTrace()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding= FragmentNoticeListBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}