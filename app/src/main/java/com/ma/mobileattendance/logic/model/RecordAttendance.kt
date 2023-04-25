package com.ma.mobileattendance.logic.model

data class RecordAttendance (
    val aId: Int,
    val sId: Int,
    val rDate: String,
    val rPunchIn: String,
    val punchInPlace: String
){
    var rId: Int? =null
    var rCategory: String="正常考勤"
    var punchOutPlace: String?=null
    var rPunchOut: String?=null
    var rResult: String="考勤中"
}