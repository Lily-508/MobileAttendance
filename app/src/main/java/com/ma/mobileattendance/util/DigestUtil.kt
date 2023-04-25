package com.ma.mobileattendance.util

import java.security.MessageDigest
object DigestUtil{
    fun md5(content:String): String {
        val digest = MessageDigest.getInstance("MD5")
        val result = digest.digest(content.toByteArray())
        val stringBuilder = StringBuilder()
        //转成16进制
        result.forEach {
            val value = it
            val hex = value.toInt() and (0xFF)
            val hexStr = Integer.toHexString(hex)
            println(hexStr)
            if(hexStr.length == 1){
                stringBuilder.append(0).append(hexStr)
            } else {
                stringBuilder.append(hexStr)
            }
        }

        return stringBuilder.toString()
    }
}
