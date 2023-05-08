package com.ma.mobileattendance.logic

import androidx.lifecycle.liveData
import com.ma.mobileattendance.MyApplication
import com.ma.mobileattendance.logic.dao.SharedPreferencesDao
import com.ma.mobileattendance.logic.model.*
import com.ma.mobileattendance.logic.network.MobileAttendanceNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    private val appDatabase: AppDatabase = AppDatabase.getDatabase(MyApplication.context)
    private val staffDao = appDatabase.staffDao()
    private var token: String? = SharedPreferencesDao.getSavedToken()

    /**
     * 获取验证码
     */
    fun getCaptcha() = liveData(Dispatchers.IO) {
        val result = try {
            val captcha = MobileAttendanceNetwork.getCaptcha()
            Result.success(captcha)
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    /**
     * 用户登录
     */
    fun login(loginData: LoginData) = liveData(Dispatchers.IO) {
        val result = try {
            val loginResponse = MobileAttendanceNetwork.login(loginData)
            Result.success(loginResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
    /**
     * 用户注销
     */
    fun logout(token: String?=this.token) = liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val loginResponse = MobileAttendanceNetwork.logout(token)
                Result.success(loginResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    /**
     * 检测token有效性
     */
    fun checkToken(token: String? = this.token) = liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val response = MobileAttendanceNetwork.checkToken(token)
                Result.success(response)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    /**
     * 用户数据存储
     */
    suspend fun insertStaff(staff: Staff) = staffDao.insertStaff(staff)
    fun selectStaffBySId(sId: Int) = staffDao.selectBySId(sId)
    suspend fun updateStaffByRoom(staff: Staff) = staffDao.updateStaff(staff)
    suspend fun deleteStaffByRoom(staff: Staff) = staffDao.deleteStaff(staff)

    /**
     * sharedPreferences存储相关
     */
    fun saveTokenAndSId(token: String, sId: Int) = SharedPreferencesDao.saveTokenAndSId(token, sId)
    fun saveRecordAttendance(recordAttendance: RecordAttendance) =
        SharedPreferencesDao.saveRecordAttendance(recordAttendance)
    fun removeRecordAttendance()=SharedPreferencesDao.removeRecordAttendance()
    fun removeTokenAndSId()=SharedPreferencesDao.removeTokenAndSId()
    fun isTokenSaved() = SharedPreferencesDao.isTokenSaved()
    fun isSIdSaved() = SharedPreferencesDao.isSIdSaved()
    fun getSId() = SharedPreferencesDao.getSavedSId()
    fun getRecordAttendance()=SharedPreferencesDao.getRecordAttendance()

    /**
     * 正常考勤和外勤考勤
     */
    fun attendancePunchIn(recordAttendance: RecordAttendance, token: String? = this.token) = liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val recordAttendanceResponse = MobileAttendanceNetwork.attendancePunchIn(token, recordAttendance)
                Result.success(recordAttendanceResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
    fun attendancePunchOut(recordAttendance: RecordAttendance, token: String? = this.token)= liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val recordAttendanceResponse = MobileAttendanceNetwork.attendancePunchOut(token, recordAttendance)
                Result.success(recordAttendanceResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
    /**
     * 获取公司Company
     */
    fun getCompanyByCId(cId: Int, token: String? = this.token)= liveData(Dispatchers.IO) {
        val result=try{
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else{
                val companyResponse=MobileAttendanceNetwork.getCompanyByCId(token,cId)
                Result.success(companyResponse)
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    /**
     * 获取考勤规则AttendanceRule
     */
    fun getAttendanceRuleByAid(aId: Int, token: String? = this.token)= liveData(Dispatchers.IO) {
        val result=try{
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else{
                val companyResponse=MobileAttendanceNetwork.getAttendanceRuleByAid(token,aId)
                Result.success(companyResponse)
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    /**
     * 拜访考勤相关
     */
    fun getVisitByDId(dId: Int, token: String? = this.token)= liveData(Dispatchers.IO) {
        val result=try{
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else{
                val dataResponse=MobileAttendanceNetwork.getVisitByDId(token,dId)
                Result.success(dataResponse)
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun setVisit(visit: Visit, token: String? = this.token) = liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val baseResponse = MobileAttendanceNetwork.setVisit(token, visit)
                Result.success(baseResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
    fun visitPunchIn(visit: Visit, token: String? = this.token) = liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val baseResponse = MobileAttendanceNetwork.visitPunchIn(token, visit)
                Result.success(baseResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
    fun visitPunchOut(visit: Visit, token: String? = this.token) = liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val baseResponse = MobileAttendanceNetwork.visitPunchOut(token, visit)
                Result.success(baseResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
    /**
     * 公告分页列表
     */
    fun getNoticePage(pageCur: Int, pageSize: Int,token: String? = this.token)= liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val pageResponse = MobileAttendanceNetwork.getNoticePage(token, pageCur,pageSize)
                Result.success(pageResponse.responseData.records)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    /**
     * 新建外派事务
     */
    fun setWorkOutside(affair: Affair,token: String? = this.token)= liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val baseResponse = MobileAttendanceNetwork.setWorkOutside(token, affair)
                Result.success(baseResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    /**
     * 获取拜访列表
     */
    fun getWorkOutsideList(sId: Int,token: String? = this.token)= liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val dataResponse = MobileAttendanceNetwork.getWorkOutsideList(token, sId)
                Result.success(dataResponse.responseData)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
    /**
     * 更新用户信息
     */
    fun updateStaff(staff: Staff,token: String? = this.token)= liveData(Dispatchers.IO) {
        val result = try {
            if (token == null) {
                Result.failure(Exception("token不存在"))
            } else {
                val baseResponse = MobileAttendanceNetwork.updateStaff(token, staff)
                Result.success(baseResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }


}