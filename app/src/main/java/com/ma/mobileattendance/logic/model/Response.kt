package com.ma.mobileattendance.logic.model


import android.graphics.Bitmap
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException

 data class BaseResponse(
    val code: Int,
    val msg: String
)
data class DataResponse<T>(
    val code: Int,
    val msg: String,
    @SerializedName("data") val responseData: T
)
data class JwtResult<T>(
    val code: Int,
    val msg: String,
    val token:String,
    @SerializedName("data") val responseData: T
)
data class PageResponse<T>(
    val code: Int,
    val msg: String,
    val total:Int,
    @SerializedName("data") val responseData: IPage<T>
)
class ErrorResponseException(message:String) :Exception(message){
    fun getResponse():BaseResponse{
        return Gson().fromJson(message,BaseResponse::class.java)
    }
}
data class IPage<T>(
    val countId: Any?,
    val current: Int,
    val maxLimit: Any?,
    val optimizeCountSql: Boolean,
    val orders: List<Any>,
    val pages: Int,
    val records: List<T>,
    val searchCount: Boolean,
    val size: Int,
    val total: Int
)