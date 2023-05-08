package com.ma.mobileattendance.ui.login

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.ma.mobileattendance.BaseActivity
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.ActivityLoginBinding
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.Captcha
import com.ma.mobileattendance.logic.model.LoginData
import com.ma.mobileattendance.ui.home.HomeActivity
import com.ma.mobileattendance.util.DigestUtil
import com.ma.mobileattendance.util.ImeiUtil
import com.ma.mobileattendance.util.showToast
import com.permissionx.guolindev.PermissionX

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    //验证码
    private var captcha: Captcha? = null
    private var imei: String? = null

    //0用户名密码 1手机验证码
    private var loginType: Int = 0
    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = resources.getString(R.string.activity_login)
        setSupportActionBar(binding.toolbar)
        initPermissionX()

    }

    private fun initPermissionX() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_PHONE_STATE,
            )
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                val message = "PermissionX需要您同意以下权限才能正常使用"
                scope.showRequestReasonDialog(deniedList, message, "确定", "取消")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白", "取消")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    //所有权限都已经成功获取
                    imei = ImeiUtil.getIMEIDeviceId(this)
                    Log.d("LoginActivity", "imei:$imei")
                    initView()
                    Toast.makeText(this, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun initView() {
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
                val loginData = LoginData(captcha!!.uuid, loginType, userId, pswLoginCode, userEncryptedPsw, imei)
                Repository.login(loginData).observe(this) { result ->
                    val loginResponse = result.getOrNull()
                    if (loginResponse?.code == 200) {
                        //存储JWT和Staff
                        viewModel.saveTokenAndSId(loginResponse.token, loginResponse.responseData.sId)
                        viewModel.insertStaff(loginResponse.responseData)
                        Log.d("ActivityBase", "登陆成功$loginResponse")
                        "登陆成功".showToast(this, Toast.LENGTH_LONG)
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (loginResponse?.code != 200) {
                        loginResponse!!.msg.showToast(this, Toast.LENGTH_SHORT)
                        Log.d("ActivityBase", "认证失败$loginResponse")
                    } else {
                        val loginException = result.exceptionOrNull()
                        loginException?.printStackTrace()
                        "登陆失败,请稍后重试".showToast(this, Toast.LENGTH_LONG)
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