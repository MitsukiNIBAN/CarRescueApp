package com.kuaijie.carrescue.util.texttospeech;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.amap.api.navi.AMapNaviListener;
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
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.LinkedList;

/**
 * Created by Mitsuki on 12-28.
 * 导航语音播报控制类
 */

public class NaviTTSController implements ICallBack, AMapNaviListener {
    private static final String TAG = "NaviTTSController";

    private final int TTS_PLAY = 1;
    private final int CHECK_TTS_PLAY = 2;

    public static NaviTTSController naviTTSManager;
    private Context context;
    private IFlyTTS iflyTTS = null;
    private TTS tts = null;
    private LinkedList<String> wordList = new LinkedList<String>();

    private NaviTTSController(Context context) {
        this.context = context.getApplicationContext();
        this.iflyTTS = IFlyTTS.getInstance(this.context);
        this.tts = this.iflyTTS;
    }

    public static NaviTTSController getInstance(Context context) {
        if (naviTTSManager == null)
            naviTTSManager = new NaviTTSController(context);
        return naviTTSManager;
    }

    public void init() {
        if (iflyTTS != null) {
            iflyTTS.init();
        }
        tts.setCallback(this);
    }

    public void stopSpeaking() {
        if (iflyTTS != null)
            iflyTTS.stopSpeak();
        wordList.clear();
    }

    public void destroy(){
        if (iflyTTS != null)
            iflyTTS.destroy();
        naviTTSManager = null;
    }

     private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TTS_PLAY:
                    if (tts != null && wordList.size() > 0) {
                        tts.playText(wordList.removeFirst());
                    }
                    break;
                case CHECK_TTS_PLAY:
                    if (!tts.isPlaying()) {
                        handler.obtainMessage(TTS_PLAY).sendToTarget();
                    }
                    break;
            }

        }
    };


    /////////////////ICallBack//////////////////////////////
    @Override
    public void onCompleted(int code) {
        if (handler != null) {
            handler.obtainMessage(TTS_PLAY).sendToTarget();
        }
    }

    ///////////////AMapNaviListener//////////////////////////
    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

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
        if (wordList != null)
            wordList.addLast(s);
        handler.obtainMessage(CHECK_TTS_PLAY).sendToTarget();
    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        if (wordList != null)
            wordList.addLast("路线规划失败");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        if (wordList != null)
            wordList.addLast("路线重新规划");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        if (wordList != null)
            wordList.addLast("前方路线拥堵，路线重新规划");
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

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
