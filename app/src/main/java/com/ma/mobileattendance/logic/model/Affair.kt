package com.ma.mobileattendance.logic.model

data class Affair (
    val sId:Int,
    val reviewer:Int,
    /**
     * 请休假假期id
     */
    val vId:Int?,
    /**
     * 考勤记录id
     */
    val rId:Int?,
    /**
     * 考勤规则id
     */
    val aId:Int?,
    val content:String?=null,
    val startTime:String,
    val endTime:String,
    val total:Long,
    val result:String=EnumNoun.AUDIT_ON_RESULT
)