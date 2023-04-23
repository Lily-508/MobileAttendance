package com.ma.mobileattendance

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.ma.mobileattendance.databinding.ActivityMainBinding
import com.ma.mobileattendance.logic.model.DataResponse
import com.ma.mobileattendance.logic.model.RecordAttendance
import com.ma.mobileattendance.logic.network.RecordAttendanceService
import com.ma.mobileattendance.logic.network.ServiceCreator
import com.permissionx.guolindev.PermissionX
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mLocClient: LocationClient
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var latLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("main", "开始运行")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPermissionX()
        binding.punchInButton.setOnClickListener {
            val recordAttendanceService =
                ServiceCreator.create(RecordAttendanceService::class.java)
            val time = System.currentTimeMillis()
            val rDate = DateFormat.format("yyyy-MM-dd", time).toString()
            val recordDateTime = DateFormat.format("yyyy-MM-dd hh:mm:ss", time).toString()
            val recordLocation = "${latLng.longitude},${latLng.latitude}"
            var recordAttendance = RecordAttendance(
                aId = 1,
                sId = 100002,
                rDate = rDate,
                rPunchIn = recordDateTime,
                punchInPlace = recordLocation
            )
            Log.d("Main", "请求体:${recordAttendance}")
            recordAttendanceService.punchAttendanceRecord(recordAttendance)
                .enqueue(object : retrofit2.Callback<DataResponse<RecordAttendance>> {
                    override fun onResponse(
                        call: Call<DataResponse<RecordAttendance>>,
                        response: Response<DataResponse<RecordAttendance>>
                    ) {
                        val body: DataResponse<RecordAttendance>? = response.body()
                        Log.d("Main", "响应体:${body}")
                        if (body != null) {
                            Toast.makeText(this@MainActivity, body.msg, Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onFailure(
                        call: Call<DataResponse<RecordAttendance>>,
                        t: Throwable
                    ) {
                        t.printStackTrace()
                    }
                })
        }

    }

    /**
     * 重置定位显示，点击之后回到自动定位
     */
    fun resetLocation(view: View) {
        initLocation()
    }

    private fun initView() {
        mBaiduMap = binding.mMapView.map
        // 开启定位图层
        mBaiduMap.isMyLocationEnabled = true
        mBaiduMap.setCompassEnable(false)

    }

    private fun initLocation() {
        //添加隐私合规政策
        LocationClient.setAgreePrivacy(true);
        // 开启定位图层
        mBaiduMap.isMyLocationEnabled = true
        // 定位初始化
        try {
            mLocClient = LocationClient(this);
        } catch (e: Exception) {
            e.printStackTrace();
        }
        val myLocationListener = MyLocationListener()
        mLocClient.registerLocationListener(myLocationListener)
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy;// 设置高精度定位
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mLocClient.locOption = option
        mLocClient.start()
    }

    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //mapView 销毁后不在处理新接收的位置
            if (location == null || binding.mMapView.map == null) {
                return
            }
            //开启指南针
            if (!mBaiduMap.uiSettings.isCompassEnabled) {
                mBaiduMap.setCompassEnable(true)
            }
            latLng = LatLng(location.latitude, location.longitude)
            binding.locationText.text = "经度:${latLng.longitude},纬度:${latLng.latitude}"
            val locDate = MyLocationData.Builder()
                .accuracy(location.radius)
                .direction(location.direction)
                .latitude(location.latitude)
                .longitude(location.longitude)
                .build()
            mBaiduMap.setMyLocationData(locDate)
            // 定位要手动移动画面到当前位置
            val mapStatus = MapStatus.Builder().target(latLng).zoom(20.0f).build()
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus))
            //定义Maker坐标点
            val point = LatLng(30.0, 120.0)
            //构建Marker图标
            val bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka)
//构建MarkerOption，用于在地图上添加Marker
            val option: OverlayOptions = MarkerOptions()
                .position(latLng)
                .icon(bitmap)
                .draggable(false)
//在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option)
            val mCircleOptions = CircleOptions().center(latLng)// 设置圆心坐标
                .setIsGradientCircle(true)
                .setCenterColor(Color.argb(0, 93, 232, 204))
                .setSideColor(Color.rgb(93, 232, 204))
                .stroke(Stroke(2, Color.rgb(93, 232, 204)))// 设置圆边框信息
                .radius(5000); //  圆半径，单位：米
            mBaiduMap.addOverlay(mCircleOptions)
        }

    }


    override fun onResume() {
        binding.mMapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.mMapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mLocClient.stop()
        mBaiduMap.isMyLocationEnabled = false
        binding.mMapView.onDestroy()
        super.onDestroy()
    }

    private fun initPermissionX() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
            )
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                val message = "PermissionX需要您同意以下权限才能正常使用"
                scope.showRequestReasonDialog(deniedList, message, "确定", "取消")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白", "取消")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    //所有权限都已经成功获取
                    //视图初始化
                    initView()
                    initLocation()
                    Toast.makeText(this, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }

            }
    }

}