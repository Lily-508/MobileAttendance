package com.ma.mobileattendance.ui.home

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ma.mobileattendance.BaseActivity
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

    }
    private fun initView() {
        setSupportActionBar(binding.toolbar)
        //bottomNavView中menu的item的id要与NavHostFragment中fragment的id保持一致
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavView.setupWithNavController(navHostFragment.navController)
        //定义多个顶层目的地,默认的起始目的地navController.graph
        val barConfig= AppBarConfiguration(setOf(R.id.nav_home,R.id.nav_punch,R.id.nav_user))
        setupActionBarWithNavController(navHostFragment.navController,barConfig)
    }
}