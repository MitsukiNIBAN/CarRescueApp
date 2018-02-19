package com.kuaijie.carrescue.util;

import android.app.Notification;
import android.app.NotificationManager;

import com.kuaijie.carrescue.R;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Mitsuki on 1-07.
 */

public class TaskNotification {

    public static final int NEW_TASK_NTF = 1;
    public static final int IMG_UP_LOAD = 2;

    private HashMap<Integer, Notification.Builder> notificationMap = new HashMap<>();

    private static class TaskNotificationHolder {
        private static final TaskNotification INSTANCE = new TaskNotification();
    }

    private TaskNotification() {
    }

    public static final TaskNotification getInstance() {
        return TaskNotificationHolder.INSTANCE;
    }

    private NotificationManager nm;

    public void init() {
        if (nm != null) {
            nm.cancelAll();
            nm = null;
        }
        nm = (NotificationManager) Application.getInstance().getSystemService(NOTIFICATION_SERVICE);
    }

    public void removeNotification(int ntfType) {
        try {
            nm.cancel(ntfType);
            notificationMap.remove(ntfType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAll() {
        try {
            nm.cancelAll();
            notificationMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新订单任务栏通知
     */
    public void newTask() {
        Notification.Builder builder = new Notification.Builder(Application.getInstance())
                .setSmallIcon(R.drawable.ic_logo)
                .setTicker("您有新的订单")// 手机状态栏的提示
                .setWhen(System.currentTimeMillis())// 设置通知时间
                .setContentTitle("您有新的订单")// 设置标题
                .setContentText("请尽快接单，5分钟后将超时无法接单")
                .setContentIntent(null)// 点击后的意图
                .setAutoCancel(true);
        //此处表示手机先震动1秒，然后静止1秒，然后再震动1秒
//        long[] vibrates = {0, 1000, 1000, 1000};
//        notification.vibrate = vibrates;
        builder.build().defaults = Notification.DEFAULT_ALL;
        nm.notify(NEW_TASK_NTF, builder.build());
        notificationMap.put(NEW_TASK_NTF, builder);
    }

    /**
     * 图片上传任务通知
     */
    public void imgUpload(Progress progress, int i) {
        if (notificationMap.get(IMG_UP_LOAD) == null) {
            //没有就新建
            Notification.Builder builder = new Notification.Builder(Application.getInstance())
                    .setSmallIcon(R.drawable.ic_logo)
                    .setTicker("正在上传图片")// 手机状态栏的提示
                    .setContentTitle("正在上传")// 设置标题
                    .setContentText((i - 1) + "个任务正在等待")
                    .setContentIntent(null)// 点击后的意图
                    .setAutoCancel(false)
                    .setOngoing(false)
                    .setProgress(100, 0, false);
            nm.notify(IMG_UP_LOAD, builder.build());
            notificationMap.put(IMG_UP_LOAD, builder);
        } else {
            //有就更新
            Notification.Builder builder = notificationMap.get(IMG_UP_LOAD);
            builder.setProgress(100, (int) (progress.fraction * 10000), false)
                    .setContentText((i - 1) + "个任务正在等待");
            nm.notify(IMG_UP_LOAD, builder.build());
        }
    }

}
