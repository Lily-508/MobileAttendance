package com.ma.mobileattendance.logic.network

import android.graphics.BitmapFactory
import com.ma.mobileattendance.logic.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object MobileAttendanceNetwork {
    private val recordAttendanceService = ServiceCreator.create<RecordAttendanceService>()
    private val loginService = ServiceCreator.create<LoginService>()
    private val companyService = ServiceCreator.create<CompanyService>()
    private val attendanceRuleService = ServiceCreator.create<AttendanceRuleService>()
    private val visitService = ServiceCreator.create<VisitService>()
    private val noticeService = ServiceCreator.create<NoticeService>()
    private val affairService = ServiceCreator.create<AffairService>()

    /**
     *获取验证码
     */
    suspend fun getCaptcha(): Captcha {
        return suspendCoroutine { continuation ->
            loginService.getCaptcha().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val body = response.body()
                    val uuid = response.headers().get("uuid")
                    if (body != null && uuid != null) {
                        val bitmap = BitmapFactory.decodeStream(body.byteStream())
                        continuation.resume(Captcha(uuid, bitmap))
                    } else {
                        continuation.resumeWithException(RuntimeException("响应异常"))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    /**
     * 登陆
     */
    suspend fun login(loginData: LoginData) = loginService.login(loginData).await()
    suspend fun checkToken(token: String) = loginService.checkToken(token).await()

    /**
     * 考勤签到和外派签到
     */
    suspend fun attendancePunchIn(token: String, recordAttendance: RecordAttendance) =
        recordAttendanceService.punchIn(token, recordAttendance).await()

    suspend fun attendancePunchOut(token: String, recordAttendance: RecordAttendance) =
        recordAttendanceService.punchOut(token, recordAttendance).await()

    /**
     * 获取公司Company
     */
    suspend fun getCompanyByCId(token: String, cId: Int) = companyService.getCompanyByCId(token, cId).await()

    /**
     * 获取考勤规则AttendanceRule
     */
    suspend fun getAttendanceRuleByAid(token: String, aId: Int) =
        attendanceRuleService.getAttendanceRuleByAid(token, aId).await()

    /**
     * 获取list拜访计划
     */
    suspend fun getVisitByDId(token: String, dId: Int) = visitService.getVisitByDId(token, dId).await()

    /**
     * 新建拜访计划
     */
    suspend fun setVisit(token: String, visit: Visit) = visitService.setVisit(token, visit).await()

    /**
     * 拜访签到
     */
    suspend fun visitPunchIn(token: String, visit: Visit) = visitService.visitPunchIn(token, visit).await()

    /**
     * 拜访签退
     */
    suspend fun visitPunchOut(token: String, visit: Visit) = visitService.visitPunchOut(token, visit).await()
    /**
     * 获取公告分页列表
     */
    suspend fun getNoticePage(token: String, pageCur: Int, pageSize: Int) =
        noticeService.getNoticePage(token, pageCur, pageSize).await()

    suspend fun setWorkOutside(token: String, affair: Affair) = affairService.setWorkOutside(token, affair).await()
    suspend fun getWorkOutsideList(token: String, sId: Int) = affairService.getWorkOutsideList(token, sId).await()


    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    val errorBody = response.errorBody()?.string()
                    if (body != null) {
                        continuation.resume(body)
                    } else if (errorBody != null) {
                        continuation.resumeWithException(ErrorResponseException(errorBody))
                    } else {
                        continuation.resumeWithException(RuntimeException("响应体为空"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }


}