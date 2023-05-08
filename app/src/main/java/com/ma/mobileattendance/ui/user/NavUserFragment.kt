package com.ma.mobileattendance.ui.user

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.ma.mobileattendance.databinding.FragmentUserBinding
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.EnumNoun
import com.ma.mobileattendance.logic.model.ErrorResponseException
import com.ma.mobileattendance.ui.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.*


class NavUserFragment : Fragment() {
    private  var _binding: FragmentUserBinding?=null
    private val binding get() = _binding!!
    private val viewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStaffData()
        binding.sBirthday.setOnClickListener {
            initDatePicker(it as TextView)
        }
        binding.sHiredate.setOnClickListener {
            initDatePicker(it as TextView)
        }
        binding.editOverBtn.setOnClickListener {
            val staff=viewModel.staff
            if(staff!=null){
                staff.sSex=binding.sSex.selectedItem.toString()
                staff.sBirthday=binding.sBirthday.text.toString()
                staff.sHiredate=binding.sHiredate.text.toString()
                staff.sEmail=binding.sEmail.text.toString()
                staff.sPhone=binding.sPhone.text.toString()
                viewModel.staff=staff
                viewModel.updateStaff()
            }
        }
        viewModel.staffLiveData.observe(viewLifecycleOwner){
            val baseResponse=it.getOrNull()
            Log.d("UserActivity","响应体$it")
            if(baseResponse!=null){
                Toast.makeText(activity,baseResponse.msg,Toast.LENGTH_LONG).show()
            }else{
                val exception=it.exceptionOrNull()
                if(exception is ErrorResponseException){
                    val errorResponse=exception.getResponse()
                    Toast.makeText(activity,errorResponse.msg,Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(activity,"更新失败,请重试",Toast.LENGTH_LONG).show()
                    exception?.printStackTrace()
                }
            }
        }
        binding.loginOutBtn.setOnClickListener {
            Repository.logout().observe(viewLifecycleOwner){ result->
                val baseResponse=result.getOrNull()
                if(baseResponse!=null){
                    Repository.removeTokenAndSId()
                    Toast.makeText(activity,baseResponse.msg, Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

    }
    private fun initStaffData(){
        val sId=Repository.getSId()
        Repository.selectStaffBySId(sId).observe(viewLifecycleOwner){
            viewModel.staff=it
            binding.sId.text="员工Id:${it.sId}"
            binding.sName.text="你好, ${it.sName}"
            binding.dId.text="部门号:${it.dId}"
            binding.sBirthday.text=it.sBirthday?:"点击选择出生日期"
            binding.sEmail.setText(it.sEmail)
            binding.sHiredate.text=it.sHiredate?:"点击选择入职日期"
            binding.sPhone.setText(it.sPhone)
            binding.sStatus.text=it.sStatus
            binding.sImei.text=it.sImei
            binding.sSex.setSelection(when(it.sSex){
                EnumNoun.MEN->0
                else->1
            })
            binding.sRight.text=when(it.sRight){
                EnumNoun.LEADER_RIGHT->"领导"
                EnumNoun.ADMIN_RIGHT->"管理员"
                else -> "普通员工"
            }
        }
    }

    private fun initDatePicker(view: TextView) {
        // 日期选择器
        val ca = Calendar.getInstance()
        val mYear = ca[Calendar.YEAR]
        val mMonth = ca[Calendar.MONTH]
        val mDay = ca[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { datePicker, _, _, _ ->
                ca.set(datePicker.year,datePicker.month,datePicker.dayOfMonth)
                val sdf= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                view.text = sdf.format(ca.time)
            },
            mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }
}