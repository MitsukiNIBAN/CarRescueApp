package com.kuaijie.carrescue.util.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by MitsukiSaMa on 10-30.
 */

public class SocketService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> SocketClient.getInstance().connect()).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
