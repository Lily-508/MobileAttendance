package com.ma.mobileattendance.logic.model

import com.google.gson.Gson

data class Company(
    val cId:Int,
    val cName:String,
    var cContent:String?=null,
    val cPlace:String
)
