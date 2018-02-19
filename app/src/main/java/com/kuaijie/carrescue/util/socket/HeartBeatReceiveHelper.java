package com.kuaijie.carrescue.util.socket;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.kuaijie.carrescue.util.Application;

/**
 * Created by MitsukiSaMa on 11-30.
 */

public class HeartBeatReceiveHelper {
    private static final int HEART_BEAT_TIME = 14 * 1000;
    public static void startReceive() {
        Log.i("HeartBeat","HeartBeatAlarmManagerReceive start");
        AlarmManager alarmManager = (AlarmManager) (Application.getInstance().getSystemService(Application.getInstance().ALARM_SERVICE));
        long triggerAtTime = SystemClock.elapsedRealtime() + HEART_BEAT_TIME;// 从开机到现在的毫秒书（手机睡眠(sleep)的时间也包括在内
        Intent i = new Intent(Application.getInstance(), HeartBeatReceive.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(Application.getInstance(), 0, i, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pIntent);
    }

    public static void stopConnectService() {
        Intent intent = new Intent();
        intent.setClass(Application.getInstance(), SocketService.class);
        Application.getInstance().stopService(intent);
    }

    public static void stopReceive() {
        Log.i("HeartBeat","HeartBeatAlarmManagerReceive stop");
        Intent intent = new Intent(Application.getInstance(), HeartBeatReceive.class);
        PendingIntent sender=PendingIntent
                .getBroadcast(Application.getInstance(), 0, intent, 0);
        AlarmManager alarm = (AlarmManager) (Application.getInstance().getSystemService(Application.getInstance().ALARM_SERVICE));
        alarm.cancel(sender);
    }
}
