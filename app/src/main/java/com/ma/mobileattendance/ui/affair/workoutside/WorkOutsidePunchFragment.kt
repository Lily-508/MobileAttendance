package com.ma.mobileattendance.ui.affair.workoutside

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.FragmentNoticeListBinding
import com.ma.mobileattendance.databinding.FragmentWorkOutsidePunchBinding
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.ErrorResponseException
import com.ma.mobileattendance.ui.notice.NoticeListAdapter
import com.ma.mobileattendance.ui.notice.NoticeViewModel
import com.ma.mobileattendance.ui.notice.NoticeViewModelFactory


class WorkOutsidePunchFragment : Fragment() {
    private  var _binding: FragmentWorkOutsidePunchBinding?=null
    private lateinit var adapter: WorkOutsidePunchAdapter
    private val binding get() = _binding!!
    private val viewModel by lazy { ViewModelProviders.of(this).get(WorkOutsidePunchViewModel::class.java) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.workOutsidePunchRecyclerView.layoutManager=LinearLayoutManager(activity)
        adapter= WorkOutsidePunchAdapter(viewModel.workOutsideList)
        binding.workOutsidePunchRecyclerView.adapter=adapter
        getWorkOutsideList()
        viewModel.sIdLiveData.observe(viewLifecycleOwner){result->
            Log.d("WorkOutsideActivity","响应体$result")
            val workOutsideList=result.getOrNull()
            if(workOutsideList!=null){
                viewModel.workOutsideList.clear()
                viewModel.workOutsideList.addAll(workOutsideList)
                adapter.notifyDataSetChanged()
            }else{
                val exception = result.exceptionOrNull()
                if (exception is ErrorResponseException) {
                    val response = exception.getResponse()
                    Toast.makeText(activity, response.msg, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(activity, "获取外派事务失败,请重试", Toast.LENGTH_LONG).show()
                    Log.d("ActivityPunch", "获取外派事务失败$exception")
                    exception?.printStackTrace()
                }
            }
        }
    }
    private fun getWorkOutsideList(){
        val sId=Repository.getSId()
        if(sId!=0){
            viewModel.getWorkOutsideList(sId)
        }else{
            Toast.makeText(activity, "获取失败,请登录!", Toast.LENGTH_LONG).show()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding= FragmentWorkOutsidePunchBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}