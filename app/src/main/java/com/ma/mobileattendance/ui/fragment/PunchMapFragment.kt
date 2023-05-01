package com.ma.mobileattendance.ui.fragment

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.FragmentPunchBinding
import com.ma.mobileattendance.databinding.FragmentPunchMapBinding
import com.ma.mobileattendance.logic.model.DataResponse
import com.ma.mobileattendance.logic.model.RecordAttendance
import com.ma.mobileattendance.logic.network.RecordAttendanceService
import com.ma.mobileattendance.logic.network.ServiceCreator
import com.permissionx.guolindev.PermissionX
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PunchMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PunchMapFragment : Fragment() {
    private  var _binding:FragmentPunchMapBinding?=null
    private val binding get() = _binding!!
    private lateinit var mLocClient: LocationClient
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var latLng: LatLng
    // TODO: Rename and change types of parameters
    private var punchType: Int? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            punchType = it.getInt("punchType")
        }
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
                            Toast.makeText(activity, body.msg, Toast.LENGTH_LONG)
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
                    Toast.makeText(activity, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private  fun initMarker(lat:Double=30.0, lng:Double=120.0, radius:Int=500){
        //定义Maker坐标点
        val point = LatLng(lat, lng)
        //构建Marker图标
        val bitmap = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka)
        //构建MarkerOption，用于在地图上添加Marker
        val option: OverlayOptions = MarkerOptions()
            .position(point)
            .icon(bitmap)
            .draggable(false)
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option)
        val mCircleOptions = CircleOptions().center(point)// 设置圆心坐标
            .setIsGradientCircle(true)
            .setCenterColor(Color.argb(0, 93, 232, 204))
            .setSideColor(Color.rgb(93, 232, 204))
            .stroke(Stroke(2, Color.rgb(93, 232, 204)))// 设置圆边框信息
            .radius(radius); //  圆半径，单位：米
        mBaiduMap.addOverlay(mCircleOptions)


    }
    private fun initView() {
        mBaiduMap = binding.mMapView.map
        // 开启定位图层
        mBaiduMap.isMyLocationEnabled = true
        mBaiduMap.setCompassEnable(false)
        //重置定位显示，点击之后回到自动定位
        binding.ibLocation.setOnClickListener{
            initLocation()
        }
        initMarker()
    }

    private fun initLocation() {
        //添加隐私合规政策
        LocationClient.setAgreePrivacy(true);
        // 开启定位图层
        mBaiduMap.isMyLocationEnabled = true
        // 定位初始化
        try {
            mLocClient = LocationClient(activity);
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

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        inflater.inflate(R.layout.fragment_punch_map, container, false)
        _binding= FragmentPunchMapBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        _binding=null
        mLocClient.stop()
        mBaiduMap.isMyLocationEnabled = false
        binding.mMapView.onDestroy()
        super.onDestroy()
    }
    override fun onResume() {
        binding.mMapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.mMapView.onPause()
        super.onPause()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PunchMapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
            PunchMapFragment().apply {
                arguments = Bundle().apply {
                    putInt("punchType", param1)
                }
            }
    }
}