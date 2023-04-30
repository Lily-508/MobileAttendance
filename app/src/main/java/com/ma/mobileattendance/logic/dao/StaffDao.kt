package com.ma.mobileattendance.logic.dao

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.room.*
import com.ma.mobileattendance.MyApplication
import com.ma.mobileattendance.logic.model.Staff

@Dao
interface StaffDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaff(staff: Staff)

    @Update
    suspend fun updateStaff(staff: Staff)

    @Delete
    suspend fun deleteStaff(staff: Staff)

    //如果在Room中方法返回值得类型定义为 LiveData 时，那么，该方法则默认是 异步 的
    @Query("select * from `staff` where s_id = :sId")
    fun selectBySId(sId: Int):LiveData<Staff>

}