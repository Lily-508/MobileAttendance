package com.ma.mobileattendance

import android.app.Application
import android.content.Context
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.common.BaiduMapSDKException
class MyApplication : Application() {
    companion object{
        @SuppressWarnings("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
        SDKInitializer.setAgreePrivacy(applicationContext,true);
        try {
            //在使用SDK各组件之前初始化context信息，传入ApplicationContext
            SDKInitializer.initialize(applicationContext);
            //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
            //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
            SDKInitializer.setCoordType(CoordType.BD09LL);
        }catch (e: BaiduMapSDKException){
            e.printStackTrace()
        }

    }
}