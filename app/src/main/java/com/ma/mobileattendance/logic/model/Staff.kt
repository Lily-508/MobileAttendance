package com.ma.mobileattendance.logic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "staff")
data class Staff(
    @PrimaryKey @ColumnInfo(name = "s_id")
    val sId: Int,
    @ColumnInfo(name = "d_id") val dId: Int,
    @ColumnInfo(name = "s_name") val sName: String,
    @ColumnInfo(name = "s_pwd")val sPwd: String,
){
    @ColumnInfo(name = "s_sex")var sSex: String?=null
    @ColumnInfo(name = "s_email")var sEmail: String?=null
    @ColumnInfo(name = "s_imei")var sImei: String?=null
    @ColumnInfo(name = "s_phone")var sPhone: String?=null
    @ColumnInfo(name = "s_birthday")var sBirthday: String?=null
    @ColumnInfo(name = "s_right")var sRight: Int?=0
    @ColumnInfo(name = "s_status")var sStatus: String?="在职"
    @ColumnInfo(name = "s_hiredate")var sHiredate: String?=null
}
