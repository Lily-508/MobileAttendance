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
    private var captcha:Captcha?=null
    //0用户名密码 1手机验证码
    private var loginType:Int=0
    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title=resources.getString(R.string.activity_login)
        setSupportActionBar(binding.toolbar)
        if(binding.captcha.drawable==null){
            viewModel.getCaptcha()
        }
        viewModel.captchaLiveData.observe(this) { result ->
            captcha = result.getOrNull()
            if (captcha != null) {
                Log.d("ActivityBase", "验证码加载成功")
                binding.captcha.setImageBitmap(captcha!!.bitmap)
            } else {
                binding.captcha.setImageResource(R.drawable.ic_launcher_background)
                "验证码加载异常,请重试".showToast(this)
                Log.d("ActivityBase","验证码加载异常")
                result.exceptionOrNull()?.printStackTrace()
            }
        }
        binding.loginBtn.setOnClickListener {
            if (binding.userId.text == null || binding.userPsw.text == null) {
                "用户ID或密码不能为空".showToast(this)
            } else if (binding.pswLoginCode.text == null||captcha==null) {
                "验证码不能为空".showToast(this)
            } else {
                val userId=binding.userId.text.toString()
                val userPsw=DigestUtil.md5(binding.userPsw.text.toString())
                val loginCode=binding.pswLoginCode.text.toString()
                val loginData=LoginData(captcha!!.uuid,loginType,userId,loginCode,userPsw)
                Repository.login(loginData).observe(this){result->
                    val loginResponse=result.getOrNull()
                    if(loginResponse!=null&&loginResponse.code==200){
                        //存储JWT和Staff
                        viewModel.saveToken(loginResponse.token)
                        viewModel.insertStaff(loginResponse.responseData)
                        Log.d("ActivityBase","登陆成功$loginResponse")
                        "登陆成功".showToast(this, Toast.LENGTH_LONG)
                        val intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }else if(loginResponse!=null&&loginResponse.code==400){
                        loginResponse.msg.showToast(this, Toast.LENGTH_SHORT)
                        Log.d("ActivityBase","认证失败$loginResponse")
                    }else{
                        Log.d("ActivityBase","登陆失败$loginData")
                        result.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }
}