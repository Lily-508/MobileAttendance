package com.ma.mobileattendance.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.ma.mobileattendance.BaseActivity
import com.ma.mobileattendance.MainActivity
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.ActivityLoginBinding
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.Captcha
import com.ma.mobileattendance.logic.model.LoginData
import com.ma.mobileattendance.util.DigestUtil
import com.ma.mobileattendance.util.showToast

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    //验证码
    private var captcha: Captcha? = null

    //0用户名密码 1手机验证码
    private var loginType: Int = 0
    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = resources.getString(R.string.activity_login)
        setSupportActionBar(binding.toolbar)
        updateCaptcha()
        binding.captcha.setOnClickListener {
            updateCaptcha()
        }
        viewModel.captchaLiveData.observe(this) { result ->
            captcha = result.getOrNull()
            if (captcha != null) {
                Log.d("ActivityBase", "验证码加载成功")
                binding.captcha.setImageBitmap(captcha!!.bitmap)
            } else {
                binding.captcha.setImageResource(R.drawable.ic_launcher_background)
                "验证码加载异常,请点击刷新".showToast(this)
                Log.d("ActivityBase", "验证码加载异常")
                result.exceptionOrNull()?.printStackTrace()
            }
        }
        binding.loginBtn.setOnClickListener {
            val userId = binding.userId.text.toString()
            val userPsw = binding.userPsw.text.toString()
            val pswLoginCode = binding.pswLoginCode.text.toString()
            if (userId == "" || userPsw == "") {
                "用户ID或密码不能为空".showToast(this)
            } else if (pswLoginCode == "" || captcha == null) {
                "验证码不能为空".showToast(this)
            } else if (userId.length < 6 || userPsw.length < 6) {
                "用户ID或密码长度错误".showToast(this)
            } else {
                val userEncryptedPsw = DigestUtil.md5(binding.userPsw.text.toString())
                val loginData = LoginData(captcha!!.uuid, loginType, userId, pswLoginCode, userEncryptedPsw)
                Repository.login(loginData).observe(this) { result ->
                    val loginResponse = result.getOrNull()
                    val loginException=result.exceptionOrNull()
                    if (loginResponse != null && loginResponse.code == 200) {
                        //存储JWT和Staff
                        viewModel.saveToken(loginResponse.token)
                        viewModel.insertStaff(loginResponse.responseData)
                        Log.d("ActivityBase", "登陆成功$loginResponse")
                        "登陆成功".showToast(this, Toast.LENGTH_LONG)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else if (loginException!=null) {
                        loginException.message?.showToast(this, Toast.LENGTH_SHORT)
                        loginException.printStackTrace()
                        Log.d("ActivityBase", "认证失败$loginException")
                    } else {
                        Log.d("ActivityBase", "登陆失败$loginData")
                    }
                }
            }
        }
    }

    private fun updateCaptcha() {
        viewModel.getCaptcha()
    }
}