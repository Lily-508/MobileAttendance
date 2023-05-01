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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
            //将数据放在Bundle中
            val bundle = Bundle()
            bundle.putInt("punchType", 0)
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NormalAttendanceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NormalAttendanceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}