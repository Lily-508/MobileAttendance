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
    fun insertStaff(staff: Staff)

    @Update
    fun updateStaff(staff: Staff)

    @Delete
    fun deleteStaff(staff: Staff)

    @Query("select * from `staff` where s_id = :sId")
    fun selectBySId(sId: Int):LiveData<Staff>

}