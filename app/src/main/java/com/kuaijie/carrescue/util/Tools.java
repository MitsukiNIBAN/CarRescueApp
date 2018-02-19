package com.kuaijie.carrescue.util;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.TypedValue;

import com.kuaijie.carrescue.ui.base.LoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MitsukiSaMa on 11-17.
 */

public class Tools {

    /**
     * dip转px
     *
     * @param dip
     * @return
     */
    public static int dip2px(float dip) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip, Application.getInstance().getResources().getDisplayMetrics()
        );
    }

    /**
     * 系统时间差
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long timeDifference(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = dateFormat.format(curDate);

            Date sysTime = dateFormat.parse(str);
            Date parse = dateFormat.parse(time);
            long interval = (parse.getTime() - sysTime.getTime()) / 1000;
            return interval;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param time     起始时间
     * @param interval 定时间隔
     * @return
     */
    public static String timing(String time, long interval, wI w) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = dateFormat.format(curDate);
            if (time == null) {
                return str;
            }
            Date sysTime = dateFormat.parse(str);
            Date parse = dateFormat.parse(time);
            if ((sysTime.getTime() - parse.getTime()) / 1000 >= interval) {
                w.work();
                return str;
            } else {
                return time;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public interface wI {
        void work();
    }


}
