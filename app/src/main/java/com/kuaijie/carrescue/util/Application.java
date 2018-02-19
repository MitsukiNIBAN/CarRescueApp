package com.kuaijie.carrescue.util;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.threadpool.TaskThreadPool;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MitsukiSaMa on 11-6.
 */

public class Application extends android.app.Application {
    public List<Activity> activityList = new ArrayList<Activity>();
    private static Application INSTANCE;

//    private Application(){}
    public synchronized static Application getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        OkGo.getInstance().init(this);
        INSTANCE = this;
        DataCache.getInstance().init();
        TaskThreadPool.getInstance().init();
        PushMsgContainer.getInstance().init();
        TaskNotification.getInstance().init();

        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5a3de75b");
    }

    public void addActivity(Activity activity)
    {
        activityList.add(activity);
    }

    public void exit() {
        for(Activity activity : activityList) {
            if(activity != null)
                activity.finish();
        }
    }

    public void finishActivity(Class<?> cls)
    {
        for(Activity activity : activityList)
        {
            if(activity != null)
            {
                if(activity.getClass().equals(cls))
                    activity.finish();
            }
        }
    }

    public Activity getActivity(Class<?> cls)
    {
        for(Activity activity : activityList)
        {
            if(activity != null)
            {
                if(activity.getClass().equals(cls))
                    return activity;
            }
        }
        return null;
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }
}
