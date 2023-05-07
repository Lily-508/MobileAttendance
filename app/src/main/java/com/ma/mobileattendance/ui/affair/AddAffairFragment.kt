package com.ma.mobileattendance.ui.affair

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.FragmentAddAffairBinding
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.Affair
import com.ma.mobileattendance.logic.model.ErrorResponseException
import com.ma.mobileattendance.logic.model.Staff
import java.text.SimpleDateFormat
import java.util.*


class AddAffairFragment : Fragment() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(AffairViewModel::class.java) }
    private var _binding: FragmentAddAffairBinding? = null
    private val binding get() = _binding!!
    private var affairType = "请休假"
    private var staff: Staff? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStaff()
        binding.startTimeText.setOnClickListener {
            initDateTimePicker(it as TextView)
        }
        binding.endTimeText.setOnClickListener {
            initDateTimePicker(it as TextView)
        }
        viewModel.totalLiveData.observe(viewLifecycleOwner) {
            binding.totalText.text = it.toString()
            if (it <= 0) {
                Toast.makeText(activity, "结束时间不能小于开始时间", Toast.LENGTH_LONG).show()
            }
        }
        binding.affairTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                affairType = parent?.getItemAtPosition(position).toString()
                when (affairType) {
                    "请休假" -> {
                        binding.attendanceRuleIdEditText.isEnabled = false
                        binding.recordAttendanceIdEditText.isEnabled = false
                        binding.vocationIdEditText.isEnabled = true
                    }
                    "加班" -> {
                        binding.attendanceRuleIdEditText.isEnabled = false
                        binding.recordAttendanceIdEditText.isEnabled = false
                        binding.vocationIdEditText.isEnabled = false
                    }
                    "外派" -> {
                        binding.attendanceRuleIdEditText.isEnabled = true
                        binding.recordAttendanceIdEditText.isEnabled = false
                        binding.vocationIdEditText.isEnabled = false
                    }
                    "补卡" -> {
                        binding.attendanceRuleIdEditText.isEnabled = false
                        binding.recordAttendanceIdEditText.isEnabled = true
                        binding.vocationIdEditText.isEnabled = false
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("AffairActivity", "spinner的adapter没有资源")
            }


        }
        binding.affairCommit.setOnClickListener {
            val affair = initAffair()
            val controller= Navigation.findNavController(it)
            Log.d("AffairActivity","$affair")
            if (affair != null) {
                when (affairType) {
                    "请休假" -> {
                    }
                    "加班" -> {
                    }
                    "外派" -> {
                        Repository.setWorkOutside(affair).observe(viewLifecycleOwner){result->
                            val response=result.getOrNull()
                            if(response!=null){
                                Toast.makeText(activity,response.msg,Toast.LENGTH_SHORT).show()
                                controller.popBackStack()
                            }else {
                                val exception = result.exceptionOrNull()
                                if (exception is ErrorResponseException) {
                                    Toast.makeText(activity, exception.getResponse().msg, Toast.LENGTH_SHORT).show()
                                    Log.d("AffairActivity", "setWorkOutside error:$exception.getResponse()")
                                } else {
                                    Log.d("AffairActivity", "setWorkOutside error:$exception")
                                    exception?.printStackTrace()
                                }
                            }
                        }
                    }
                    "补卡" -> {
                    }
                }
            }else{
                Toast.makeText(activity, "表单参数格式错误!", Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun initTimePickerDialog(view:TextView, datePicker:DatePicker){
        val ca = Calendar.getInstance()
        val mHour = ca[Calendar.HOUR_OF_DAY]
        val mMinute = ca[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                ca.set(datePicker.year,datePicker.month,datePicker.dayOfMonth,hourOfDay,minute,0)
                val sdf=SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault())
                view.text = sdf.format(ca.time)
                if (view.id == R.id.endTimeText) {
                    val startTime = binding.startTimeText.text.toString()
                    viewModel.computeTotal(startTime, view.text.toString())
                }
            },
            mHour, mMinute, true
        )
        timePickerDialog.show()
    }
    private fun initDateTimePicker(view: TextView) {
        // 日期选择器
        val ca = Calendar.getInstance()
        val mYear = ca[Calendar.YEAR]
        val mMonth = ca[Calendar.MONTH]
        val mDay = ca[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { datePicker, _, _, _ ->
                initTimePickerDialog(view,datePicker)
            },
            mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    private fun initStaff() {
        val sId = Repository.getSId()
        if (sId != 0) {
            val staffLiveData = Repository.selectStaffBySId(sId)
            staffLiveData.observe(viewLifecycleOwner) {
                staff = it
                binding.staffNameText.text = it.sName
                binding.staffIdText.text = it.sId.toString()
            }
        } else {
            Toast.makeText(activity, "获取缓存失败,请登录", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initAffair(): Affair? {
        try {
            val sId = binding.staffIdText.text.toString().toInt()
            val reviewer = binding.reviewerIdEditText.text.toString().toInt()
            val vocationIdEditText=binding.vocationIdEditText.text.toString()
            val vId = if(vocationIdEditText.isEmpty()){
                0
            }else{
                vocationIdEditText.toInt()
            }
            val reviewerIdEditText=binding.reviewerIdEditText.text.toString()
            val rId = if(reviewerIdEditText.isEmpty()){
                0
            }else{
                reviewerIdEditText.toInt()
            }
            val attendanceRuleIdEditText=binding.attendanceRuleIdEditText.text.toString()
            val aId =if(attendanceRuleIdEditText.isEmpty()){
                0
            }else{
                attendanceRuleIdEditText.toInt()
            }
            val content = binding.contentEditText.text.toString()
            val startTime = binding.startTimeText.text.toString()
            val endTime = binding.endTimeText.text.toString()
            val total = binding.totalText.text.toString().toLong()
            if (sId == 0 || reviewer == 0 || total <= 0) return null
            return Affair(sId, reviewer, vId, rId, aId, content, startTime, endTime, total)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            return null
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAffairBinding.inflate(inflater, container, false)
        return binding.root
    }

}