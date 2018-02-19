package com.kuaijie.carrescue.util.socket;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.kuaijie.carrescue.util.Application;

public class HeartBeatReceive extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("HeartBeat","Go To HeartBeat AlarmManager Service");
        Intent i = new Intent(context, HeartBeatService.class);
        context.startService(i);
    }
}