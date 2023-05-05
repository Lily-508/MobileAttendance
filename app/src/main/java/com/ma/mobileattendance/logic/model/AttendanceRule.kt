package com.ma.mobileattendance.logic.model

data class AttendanceRule(
    val aId:Int,
    val cId:Int,
    val aStart:String,
    val aEnd:String,
    val locationRange:Int,
    val exceptionRange:Int,
    val neglectRange:Int
)
