package com.ma.mobileattendance.logic.model

data class Visit(
    val cId: Int,
    val dId: Int,
    val vStart: String,
    val vEnd: String,

){
    var vId:Int?=null
    var vContent:String?=null
    var rPunchIn: String?=null
    var punchInPlace: String?=null
    var punchOutPlace: String?=null
    var rPunchOut: String?=null
}
