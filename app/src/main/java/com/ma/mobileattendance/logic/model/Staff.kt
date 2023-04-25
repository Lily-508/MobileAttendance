package com.ma.mobileattendance.logic.model

data class Staff(
    val sId: Int,
    val dId: Int,
    val sName: String,
    val sPwd: String,
){
    var sEmail: String?=null
    var sPhone: String?=null
    var sRight: Int?=0
    var sSex: String?=null
    var sStatus: String?="在职"
}
