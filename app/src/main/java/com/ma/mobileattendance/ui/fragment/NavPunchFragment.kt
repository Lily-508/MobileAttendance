package com.ma.mobileattendance.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.FragmentPunchBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NavPunchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavPunchFragment : Fragment() {
    private  var _binding: FragmentPunchBinding?=null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.normalAttendance.setOnClickListener {
            val controller= Navigation.findNavController(it)
            controller.navigate(R.id.action_nav_punch_to_normalAttendanceFragment)
        }
        binding.outsideAttendance.setOnClickListener {
            val controller= Navigation.findNavController(it)
            controller.navigate(R.id.action_nav_punch_to_workOutsidePunchFragment)
        }
        binding.visitAttendance.setOnClickListener {
            Toast.makeText(activity,"待完善!",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        inflater.inflate(R.layout.fragment_punch,container,false)
        _binding= FragmentPunchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        _binding=null
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentPunch.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NavPunchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}