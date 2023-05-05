package com.ma.mobileattendance.logic.model

data class RecordAttendance (
    val aId: Int,
    val sId: Int,
    val rDate: String,
    val rPunchIn: String,
    val punchInPlace: String
){
    var rId: Int? =null
    var rCategory: String=EnumNoun.NORMAL_PUNCH
    var punchOutPlace: String?=null
    var rPunchOut: String?=null
    var rResult: String=EnumNoun.PUNCH_ON_RESULT
}
