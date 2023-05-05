package com.ma.mobileattendance.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ma.mobileattendance.BaseActivity
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.ActivityHomeBinding
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.ui.login.LoginActivity
import com.ma.mobileattendance.util.showToast

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    override fun onStart() {
        super.onStart()
        checkLogin()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun checkLogin() {
        if (Repository.isSIdSaved() && Repository.isTokenSaved()) {
            Repository.checkToken().observe(this) { result ->
                val response = result.getOrNull()
                if (response?.code == 200) {
                    "token校验成功".showToast(this)
                } else if (response?.code == 401) {
                    response.msg.showToast(this)
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    val exception = result.exceptionOrNull()
                    Log.d("HomeActivity", "token校验出现异常$exception")
                    "token校验异常,请登录".showToast(this)
                    exception?.printStackTrace()
                }
            }
        } else {
            "请前往登陆".showToast(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        //bottomNavView中menu的item的id要与NavHostFragment中fragment的id保持一致
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavView.setupWithNavController(navHostFragment.navController)
        //定义多个顶层目的地,默认的起始目的地navController.graph
        val barConfig = AppBarConfiguration.Builder(setOf(R.id.nav_home, R.id.nav_punch, R.id.nav_user))
            .setFallbackOnNavigateUpListener {
                true
            }
            .build()
        setupActionBarWithNavController(navHostFragment.navController, barConfig)
    }
}