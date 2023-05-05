package com.ma.mobileattendance.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.FragmentNormalAttendanceBinding
import com.ma.mobileattendance.databinding.FragmentPunchBinding
import com.ma.mobileattendance.logic.model.EnumNoun


/**
 * A simple [Fragment] subclass.
 * Use the [NormalAttendanceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NormalAttendanceFragment : Fragment() {
    private  var _binding: FragmentNormalAttendanceBinding?=null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.normalPunchBtn.setOnClickListener {
            //将数据放在Bundle中,
            val bundle = Bundle()
            bundle.putString("rCategory", EnumNoun.NORMAL_PUNCH)
            bundle.putInt("recordRuleId", EnumNoun.NORMAL_PUNCH_RULE)
            val controller= Navigation.findNavController(it)
            controller.navigate(R.id.action_normalAttendanceFragment_to_punchMapFragment,bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding= FragmentNormalAttendanceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}