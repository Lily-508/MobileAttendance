package com.ma.mobileattendance.ui.fragment

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.SpatialRelationUtil
import com.ma.mobileattendance.R
import com.ma.mobileattendance.databinding.FragmentPunchMapBinding
import com.ma.mobileattendance.logic.Repository
import com.ma.mobileattendance.logic.model.*
import com.permissionx.guolindev.PermissionX


/**
 * A simple [Fragment] subclass.
 * Use the [PunchMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PunchMapFragment : Fragment() {
    private var _binding: FragmentPunchMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mLocClient: LocationClient
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var userLatLng: LatLng
    private lateinit var companyLatLng: LatLng
    private lateinit var attendanceRule: AttendanceRule
    private lateinit var company: Company

    private var rCategory: String? = ""
    private var recordRuleId: Int = 0

    private fun initPunchIn() {
        when (rCategory) {
            EnumNoun.NORMAL_PUNCH, EnumNoun.OUTSIDE_PUNCH -> {
                recordAttendancePunchIn()
            }
            EnumNoun.VISIT_PUNCH -> {
                visitPunchIn()
            }
            else -> {
                Toast.makeText(activity, "缺少必要参数", Toast.LENGTH_LONG).show()
                Log.d("PunchActivity", "rCategory error $rCategory")
            }
        }
    }

    private fun initPunchOut() {
        when (rCategory) {
            EnumNoun.NORMAL_PUNCH, EnumNoun.OUTSIDE_PUNCH -> {
                recordAttendancePunchOut()
            }
            EnumNoun.VISIT_PUNCH -> {
                visitPunchOut()
            }
            else -> {
                Toast.makeText(activity, "缺少必要参数", Toast.LENGTH_LONG).show()
                Log.d("PunchActivity", "rCategory error $rCategory")
            }
        }
    }

    private fun visitPunchIn() {
        //跳转进来自带visit类型(完成这个逻辑),直接拜访签到
    }

    private fun visitPunchOut() {

    }

    private fun recordAttendancePunchIn() {
        val recordAttendance = initPunchInRecordAttendance()
        Log.d("ActivityPunch", "recordAttendancePunchIn请求体:${recordAttendance}")
        if (recordAttendance != null) {
            Repository.attendancePunchIn(recordAttendance).observe(viewLifecycleOwner) { result ->
                val dataResponse = result.getOrNull()
                Log.d("ActivityPunch", "$result")
                if (dataResponse != null) {
                    when (dataResponse.code) {
                        200 -> {
                            Toast.makeText(activity, dataResponse.msg, Toast.LENGTH_LONG).show()
                            Repository.saveRecordAttendance(dataResponse.responseData)
                        }
                        else -> {
                            Toast.makeText(activity, dataResponse.msg, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    if (exception is ErrorResponseException) {
                        val response = exception.getResponse()
                        Toast.makeText(activity, response.msg, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(activity, "签到失败,请重试", Toast.LENGTH_LONG).show()
                        Log.d("ActivityPunch", "签到失败$exception")
                        exception?.printStackTrace()
                    }
                }
            }
        }
    }

    private fun recordAttendancePunchOut() {
        val recordAttendance = initPunchOutRecordAttendance()
        Log.d("ActivityPunch", "recordAttendancePunchOut请求体:${recordAttendance}")
        if (recordAttendance != null) {
            Repository.attendancePunchOut(recordAttendance).observe(viewLifecycleOwner) { result ->
                val dataResponse = result.getOrNull()
                if (dataResponse != null) {
                    when (dataResponse.code) {
                        200 -> {
                            Toast.makeText(activity, dataResponse.msg, Toast.LENGTH_LONG).show()
                            Repository.removeRecordAttendance()
                        }
                        else -> {
                            Toast.makeText(activity, dataResponse.msg, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    if (exception is ErrorResponseException) {
                        val response = exception.getResponse()
                        Toast.makeText(activity, response.msg, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(activity, "签退失败,请重试", Toast.LENGTH_LONG).show()
                        Log.d("ActivityPunch", "签退失败$exception")
                        exception?.printStackTrace()
                    }
                }
            }
        }
    }

    private fun initPunchOutRecordAttendance(): RecordAttendance? {
        val sId = Repository.getSId()
        val recordAttendance = Repository.getRecordAttendance()
        return if (recordAttendance != null && recordAttendance.sId == sId && recordAttendance.rCategory == rCategory
            && recordAttendance.aId == recordRuleId && recordAttendance.rId != null
        ) {
            val time = System.currentTimeMillis()
            val recordDateTime = DateFormat.format("yyyy-MM-dd hh:mm:ss", time).toString()
            val recordLocation = "${userLatLng.longitude},${userLatLng.latitude}"
            recordAttendance.punchOutPlace = recordLocation
            recordAttendance.rPunchOut = recordDateTime
            decideRecordResult(recordAttendance)
            recordAttendance
        } else {
            Toast.makeText(activity, "请先签到", Toast.LENGTH_LONG).show()
            Log.d("PunchActivity", "错误的$recordAttendance")
            null
        }
    }

    private fun initPunchInRecordAttendance(): RecordAttendance? {
        val time = System.currentTimeMillis()
        val rDate = DateFormat.format("yyyy-MM-dd", time).toString()
        val recordDateTime = DateFormat.format("yyyy-MM-dd hh:mm:ss", time).toString()
        val recordLocation = "${userLatLng.longitude},${userLatLng.latitude}"
        val sId = Repository.getSId()
        if (sId == 0 || rCategory == "" || recordRuleId == 0) {
            Toast.makeText(activity, "缺少必要参数,请登录", Toast.LENGTH_LONG).show()
            Log.d("PunchActivity", "缺少必要参数,请登录")
            val navController = Navigation.findNavController(requireView())
            if (!navController.popBackStack()) {
                requireActivity().onBackPressed()
            }
            return null
        } else {
            val recordAttendance = RecordAttendance(
                recordRuleId,
                sId,
                rDate,
                rPunchIn = recordDateTime,
                punchInPlace = recordLocation,
            )
            recordAttendance.rCategory = rCategory!!
            return recordAttendance
        }
    }

    /**
     * 判断签到地点是否在范围内
     */
    private fun decideRecordResult(recordAttendance: RecordAttendance) {
        when (SpatialRelationUtil.isCircleContainsPoint(companyLatLng, attendanceRule.locationRange, userLatLng)) {
            true -> {
                recordAttendance.rResult = EnumNoun.PUNCH_SUCCESS_RESULT
            }
            false -> {
                recordAttendance.rResult = EnumNoun.PUNCH_FAILURE_RESULT
            }
        }
    }

    /**
     * 获取跳转参数判断获取考勤规则进行不同类别的考勤
     */
    private fun getRecordRule() {
        if (recordRuleId != 0) {
            Repository.getAttendanceRuleByAid(recordRuleId).observe(viewLifecycleOwner) { result ->
                Log.d("ActivityPunch", "getAttendanceRuleByAid:$result")
                val dataResponse = result.getOrNull()
                if (dataResponse != null && dataResponse.code == 200) {
                    attendanceRule = dataResponse.responseData
                    getCompanyPlace(attendanceRule.cId)
                } else {
                    Toast.makeText(activity,"网络错误",Toast.LENGTH_SHORT).show()
                    val exception = result.exceptionOrNull()
                    if (exception is ErrorResponseException) {
                        val response = exception.getResponse()
                        Log.d("ActivityPunch", "getAttendanceRuleByAid error:$response")
                    } else {
                        Log.d("ActivityPunch", "getAttendanceRuleByAid error:$exception")
                        exception?.printStackTrace()
                    }
                }
            }
        } else {
            Log.d("ActivityPunch", "recordRuleId error:$recordRuleId")
        }
    }

    private fun getCompanyPlace(cId: Int) {
        Repository.getCompanyByCId(cId).observe(viewLifecycleOwner) { result ->
            Log.d("ActivityPunch", "getAttendanceRuleByAid:$result")
            val dataResponse = result.getOrNull()
            if (dataResponse != null && dataResponse.code == 200) {
                company = dataResponse.responseData
                val (lng, lat) = company.cPlace.split(",")
                //定义Maker坐标点
                companyLatLng = LatLng(lat.toDouble(), lng.toDouble())
                initMarker()
            } else {
                val exception = result.exceptionOrNull()
                if (exception is ErrorResponseException) {
                    val response = exception.getResponse()
                    Log.d("ActivityPunch", "getCompanyByCId error:$response")
                } else {
                    Log.d("ActivityPunch", "getCompanyByCId error:$exception")
                    exception?.printStackTrace()
                }
            }
        }
    }

    private fun initMarker() {
        //构建Marker图标
        val bitmap = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka)
        //构建MarkerOption，用于在地图上添加Marker
        val option: OverlayOptions = MarkerOptions()
            .position(companyLatLng)
            .icon(bitmap)
            .draggable(false)
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option)
        val mCircleOptions = CircleOptions().center(companyLatLng)// 设置圆心坐标
            .setIsGradientCircle(true)
            .setCenterColor(Color.argb(0, 93, 232, 204))
            .setSideColor(Color.rgb(93, 232, 204))
            .stroke(Stroke(2, Color.rgb(93, 232, 204)))// 设置圆边框信息
            .radius(attendanceRule.locationRange); //  圆半径，单位：米
        mBaiduMap.addOverlay(mCircleOptions)
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

    private fun initMapView() {
        mBaiduMap = binding.mMapView.map
        // 开启定位图层
        mBaiduMap.isMyLocationEnabled = true
        mBaiduMap.setCompassEnable(false)
        initLocation()
        getRecordRule()
        binding.punchInButton.setOnClickListener {
            initPunchIn()
        }
        binding.punchOutButton.setOnClickListener {
            initPunchOut()
        }
        //重置定位显示，点击之后回到自动定位
        binding.iconLocation.setOnClickListener {
            initLocation()
        }
        binding.iconMarkb.setOnClickListener {
            val mapStatus = MapStatus.Builder().target(companyLatLng).zoom(20.0f).build()
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus))
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
                    initMapView()

                    Toast.makeText(activity, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }

            }
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
            userLatLng = LatLng(location.latitude, location.longitude)
            binding.locationText.text = "经度:${userLatLng.longitude},纬度:${userLatLng.latitude}"
            val locDate = MyLocationData.Builder()
                .accuracy(location.radius)
                .direction(location.direction)
                .latitude(location.latitude)
                .longitude(location.longitude)
                .build()
            mBaiduMap.setMyLocationData(locDate)
            // 定位要手动移动画面到当前位置
            val mapStatus = MapStatus.Builder().target(userLatLng).zoom(20.0f).build()
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus))

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            rCategory = it.getString("rCategory")
            recordRuleId = it.getInt("recordRuleId")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPermissionX()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        inflater.inflate(R.layout.fragment_punch_map, container, false)
        _binding = FragmentPunchMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        mLocClient.stop()
        mBaiduMap.isMyLocationEnabled = false
        binding.mMapView.onDestroy()
        _binding = null
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