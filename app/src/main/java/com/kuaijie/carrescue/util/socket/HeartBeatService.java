package com.kuaijie.carrescue.util.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by MitsukiSaMa on 11-30.
 */

public class HeartBeatService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("HeartBeat","HeartBeat Service Create");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("HeartBeat","HeartBeat Service Start");
        new Thread(() -> {
            Log.i("HeartBeat","Send HeartBeat Msg");
            SocketClient.getInstance().sendHeartBeat();
            HeartBeatReceiveHelper.startReceive();
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
