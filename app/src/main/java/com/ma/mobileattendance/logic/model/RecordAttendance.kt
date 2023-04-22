package com.ma.mobileattendance.logic.model

data class RecordAttendance (
    var aId: Int,
    var sId: Int,
    var rDate: String,
    var rPunchIn: String,
    var punchInPlace: String
){
    var rId: Int? =null
    var rCategory: String="正常考勤"
    var punchOutPlace: String?=null
    var rPunchOut: String?=null
    var rResult: String="考勤中"
}