package com.ma.mobileattendance.logic.model

data class Notice(
    val nId:Int,
    val nTitle:String,
    val nContent:String?=null,
    val nDatetime:String,
)