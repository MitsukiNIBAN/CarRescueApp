package com.kuaijie.carrescue.ui.technician;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.Other;
import com.kuaijie.carrescue.databinding.ActivityNavigationBinding;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.ErrorInfo;
import com.kuaijie.carrescue.util.texttospeech.NaviTTSController;

import java.util.ArrayList;
import java.util.List;

import static com.amap.api.navi.enums.PathPlanningStrategy.DRIVING_AVOID_CONGESTION;

/**
 * Created by Mitsuki on 12-26.
 */

public class NavigationActivity extends Activity implements AMapNaviViewListener, AMapNaviListener {

    private static final String TAG = "NavigationActivity";

    private ActivityNavigationBinding binding;
    private AMapNavi aMapNavi;
    private NaviTTSController naviTTSController;

    private final List<NaviLatLng> sList = new ArrayList<>();
    private final List<NaviLatLng> eList = new ArrayList<>();
    private List<NaviLatLng> mWayPointList;

//    private NaviLatLng mEndLatlng = new NaviLatLng(29.862767, 121.527336);
    private NaviLatLng mStartLatlng = new NaviLatLng(29.911660, 121.610220);

    private float latitude = 29.9116600000f;
    private float longitude = 21.6102200000f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_navigation);

        Intent in = getIntent();
        latitude = in.getFloatExtra("latitude" , 29.9116600000f);
        longitude = in.getFloatExtra("longitude", 21.6102200000f);

        NaviLatLng mEndLatlng = new NaviLatLng(latitude, longitude);

        naviTTSController = NaviTTSController.getInstance(getApplicationContext());
        naviTTSController.init();
        aMapNavi = AMapNavi.getInstance(getApplicationContext());
        aMapNavi.addAMapNaviListener(this);
        aMapNavi.addAMapNaviListener(naviTTSController);

        aMapNavi.setEmulatorNaviSpeed(75);
        sList.add(mStartLatlng);
        eList.add(mEndLatlng);

        binding.nttvMydirectionView.setCustomIconTypes(getResources(), Other.customIconTypes);

        binding.naviView.setLazyNextTurnTipView(binding.nttvMydirectionView);
        binding.naviView.setLazyOverviewButtonView(binding.obvMyOverviewButtonView);
        binding.naviView.setLazyZoomButtonView(binding.zbvMyZoomButtonView);

        binding.naviView.onCreate(savedInstanceState);
        binding.naviView.setAMapNaviViewListener(this);
        AMapNaviViewOptions options = binding.naviView.getViewOptions();
        //隐藏SDK默认的布局
        options.setLayoutVisible(false);
        binding.naviView.setViewOptions(options);

        binding.btnNaviCtn.setOnClickListener(view -> binding.naviView.recoverLockMode());
        binding.btnExitNavi.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.naviView.onResume();
        aMapNavi.resumeNavi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.naviView.onPause();
        aMapNavi.pauseNavi();
        naviTTSController.stopSpeaking();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.naviView.onDestroy();
        aMapNavi.stopNavi();
        aMapNavi.destroy();
        naviTTSController.destroy();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出导航吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", (dialog, which) -> {
                    // 点击“确认”后的操作
                    finish();

                })
                .setNegativeButton("返回", (dialog, which) -> {
                    // 点击“返回”后的操作,这里不设置没有任何操作
                }).setIcon(R.drawable.ic_warning).show();
    }


    //////////////////////AMapNaviViewListener//////////////////////////
    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {
        Log.e(TAG, "LOCK:" + b);
        if (b) {
            binding.tvNaviInfo.setVisibility(View.VISIBLE);
            binding.btnNaviCtn.setVisibility(View.GONE);
            binding.zbvMyZoomButtonView.setVisibility(View.GONE);
            binding.obvMyOverviewButtonView.setVisibility(View.GONE);
        } else {
            binding.tvNaviInfo.setVisibility(View.GONE);
            binding.btnNaviCtn.setVisibility(View.VISIBLE);
            binding.zbvMyZoomButtonView.setVisibility(View.VISIBLE);
            binding.obvMyOverviewButtonView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNaviViewLoaded() {
        Log.i(TAG, "导航页面加载成功");
    }

    @Override
    public void onInitNaviFailure() {

    }

    /////////////////////AMapNaviListener//////////////////////////////
    @Override
    public void onInitNaviSuccess() {
        Log.i(TAG, "onInitNaviSuccess");
        aMapNavi.calculateDriveRoute(sList, eList, null, DRIVING_AVOID_CONGESTION);
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {
    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        Log.i(TAG, "onCalculateRouteFailure");
        Log.e("dm", "--------------------------------------------");
        Log.i("dm", "路线计算失败：错误码=" + i + ",Error Message= " + ErrorInfo.getError(i));
        Log.i("dm", "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.e("dm", "--------------------------------------------");
//        Toast.makeText(this, "errorInfo：" + i + ",Message：" + ErrorInfo.getError(i), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "获取线路失败，请重新开始导航", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        Log.i(TAG, "onNaviInfoUpdate");
        binding.tvCurStepRetainDistance.setText(naviInfo.getCurStepRetainDistance() + "米后 进入");
        binding.tvNextRoad.setText(naviInfo.getNextRoadName() + "");
        binding.tvSpeed.setText(naviInfo.getCurrentSpeed() + "km/h");
        binding.tvNaviRoad.setText(naviInfo.getCurrentRoadName() + "");
        binding.tvNaviInfo.setText("剩余" + (naviInfo.getPathRetainDistance()/1000f) + "公里 "
                + naviInfo.getCurStepRetainTime() / 3600 + "时" + naviInfo.getCurStepRetainTime() % 3600 / 60 + "分");
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
    }

    @Override
    public void hideCross() {
    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        Log.i(TAG, "onCalculateRouteSuccess");
//        aMapNavi.startNavi(NaviType.GPS);
        //测试用模拟导航
        aMapNavi.startNavi(NaviType.EMULATOR);
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
    }

    @Override
    public void onPlayRing(int i) {

    }

}
