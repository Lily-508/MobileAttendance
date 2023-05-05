package com.ma.mobileattendance.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.FragmentHomeBinding
import com.ma.mobileattendance.databinding.FragmentNoticeListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NavHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavHomeFragment : Fragment() {
    private  var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.noticeListButton.setOnClickListener {
            val controller= Navigation.findNavController(it)
            controller.navigate(R.id.action_nav_home_to_noticeListFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

}